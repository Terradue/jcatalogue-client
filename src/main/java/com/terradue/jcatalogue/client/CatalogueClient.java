package com.terradue.jcatalogue.client;

/*
 *    Copyright 2011 Terradue srl
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import static com.terradue.jcatalogue.client.utils.Assertions.checkArgument;
import static com.terradue.jcatalogue.client.utils.Assertions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.beanutils.ConvertUtils.register;
import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.ssl.KeyMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Realm;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import com.ning.http.client.resumable.ResumableIOExceptionFilter;
import com.terradue.jcatalogue.client.download.DownloadHandler;
import com.terradue.jcatalogue.client.download.Downloader;
import com.terradue.jcatalogue.client.download.HttpDownloader;
import com.terradue.jcatalogue.client.download.Protocol;
import com.terradue.jcatalogue.client.geo.Line;
import com.terradue.jcatalogue.client.geo.Point;
import com.terradue.jcatalogue.client.geo.Polygon;
import com.terradue.jcatalogue.client.internal.converters.AtomDateConverter;
import com.terradue.jcatalogue.client.internal.converters.CharsetConverter;
import com.terradue.jcatalogue.client.internal.converters.GeoConverter;
import com.terradue.jcatalogue.client.internal.converters.LocaleConverter;
import com.terradue.jcatalogue.client.internal.digester.AtomRulesModule;
import com.terradue.jcatalogue.client.internal.digester.DataSetRulesModule;
import com.terradue.jcatalogue.client.internal.digester.LinkedAtomEntityModule;
import com.terradue.jcatalogue.client.internal.digester.OpenSearchModule;
import com.terradue.jcatalogue.client.internal.digester.SingleDataSetRulesModule;
import com.terradue.jcatalogue.client.umsso.Credentials;

public final class CatalogueClient
{

    static
    {
        register( new AtomDateConverter(), Date.class );
        register( new LocaleConverter(), Locale.class );
        register( new CharsetConverter(), Charset.class );

        Converter geoConverter = new GeoConverter();
        register( geoConverter, Line.class );
        register( geoConverter, Point.class );
        register( geoConverter, Polygon.class );
    }

    private final Map<String, Downloader> downloaders = new HashMap<String, Downloader>();

    private final Logger logger = LoggerFactory.getLogger( getClass() );

    /**
     * @since 0.7
     */
    private final Map<String, Realm> realms = new HashMap<String, Realm>();

    /**
     * @since 0.7
     */
    private final List<KeyManager> keyManagers = new ArrayList<KeyManager>();

    /**
     * @since 0.7
     */
    private final TrustManager[] trustManagers = new TrustManager[] { new DummyTrustManager() };

    /**
     * @since 0.8
     */
    private final Map<String, Credentials> umSsoCredentials = new HashMap<String, Credentials>();

    private final DigesterLoader descriptionDigesterLoader;

    private final DigesterLoader catalogueDigesterLoader;

    private final DigesterLoader serieDigesterLoader;

    private final DigesterLoader singleDataSetDigesterLoader;

    private final AsyncHttpClient httpClient;

    public CatalogueClient()
    {
        descriptionDigesterLoader = newLoader( new OpenSearchModule() ).setNamespaceAware( true );
        catalogueDigesterLoader = newLoader( new AtomRulesModule( Catalogue.class ), new LinkedAtomEntityModule() )
            .setNamespaceAware( true );
        serieDigesterLoader = newLoader( new AtomRulesModule( Series.class ), new DataSetRulesModule() ).setNamespaceAware( true );
        singleDataSetDigesterLoader = newLoader( new SingleDataSetRulesModule() ).setNamespaceAware( true );

        SSLContext context = null;
        try
        {
            context = SSLContext.getInstance( "TLS" );
            context.init( new KeyManager[] {}, trustManagers, null );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "Impossible to initialize SSL context", e );
        }

        httpClient = new AsyncHttpClient( new AsyncHttpClientConfig.Builder()
                                                            .setAllowPoolingConnection( true )
                                                            .addIOExceptionFilter( new ResumableIOExceptionFilter() )
                                                            .setMaximumConnectionsPerHost( 10 )
                                                            .setMaximumConnectionsTotal( 100 )
                                                            .setFollowRedirects( true )
                                                            .addResponseFilter( new StatusResponseFilter() )
                                                            .setSSLContext( context )
                                                            .build() );

        registerDownloader( new HttpDownloader( httpClient, realms ) );
    }

    private static void checkFile( File file )
    {
        checkArgument( file.exists(), "File %s not found, please verify it exists", file );
        checkArgument( !file.isDirectory(), "File %s must be not a directory", file );
    }

    public void registerDownloader( Downloader downloader )
    {
        downloader = checkNotNull( downloader, "Input downloader cannot be null" );
        checkArgument( downloader.getClass().isAnnotationPresent( Protocol.class ),
                       "Class %s must be annotated with %s", downloader.getClass().getName(), Protocol.class.getName() );

        for ( String protocol : downloader.getClass().getAnnotation( Protocol.class ).value() )
        {
            registerDownloader( protocol, downloader );
        }
    }

    public void registerDownloader( String protocol, Downloader downloader )
    {
        protocol = checkNotNull( protocol, "Input protocol cannot be null" );
        downloader = checkNotNull( downloader, "Input downloader cannot be null" );

        downloaders.put( protocol, downloader );
    }

    public <D extends Downloader> D lookupDownloader( String protocol )
    {
        protocol = checkNotNull( protocol, "Input protocol cannot be null" );

        if ( !downloaders.containsKey( protocol ) )
        {
            return null;
        }

        @SuppressWarnings( "unchecked" ) // would throw classcast exception anyway
        D downloader = (D) downloaders.get( protocol );
        return downloader;
    }

    // Description methods

    public CatalogueDescription discover( String uri, Parameter... parameters )
    {
        return invoke( descriptionDigesterLoader, uri, parameters );
    }

    public CatalogueDescription discover( URI uri, Parameter...parameters )
    {
        return invoke( descriptionDigesterLoader, uri, parameters );
    }

    // Catalogue methods

    public Catalogue getCatalogue( String uri, Parameter... parameters )
    {
        return invoke( catalogueDigesterLoader, uri, parameters );
    }

    public Catalogue getCatalogue( URI uri, Parameter... parameters )
    {
        return invoke( catalogueDigesterLoader, uri, parameters );
    }

    // Series methods

    public Series getSeries( String uri, Parameter... parameters )
    {
        return invoke( serieDigesterLoader, uri, parameters );
    }

    public Series getSeries( URI uri, Parameter... parameters )
    {
        return invoke( serieDigesterLoader, uri, parameters );
    }

    // DataSet methods

    /**
     * @since 0.2
     */
    public DataSet getDataSet( String uri, Parameter... parameters )
    {
        return invoke( singleDataSetDigesterLoader, uri, parameters );
    }

    /**
     * @since 0.2
     */
    public DataSet getDataSet( URI uri, Parameter... parameters )
    {
        return invoke( singleDataSetDigesterLoader, uri, parameters );
    }

    // generic internal methods

    <CE extends CatalogueEntity> CE invoke( final DigesterLoader digesterLoader, String uri, Parameter...parameters )
    {
        uri = checkNotNull( uri, "Input URI cannot be null" );

        try
        {
            return invoke( digesterLoader, new URI( uri ), parameters );
        }
        catch ( URISyntaxException e )
        {
            throw new RuntimeException( uri + " is not a valid URI", e );
        }
    }

    <CE extends CatalogueEntity> CE invoke( final DigesterLoader digesterLoader, final URI uri, Parameter...parameters )
    {
        checkNotNull( uri, "Input URI cannot be null" );

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Invoking Catalogue URI '{}' with parameters: ", uri, Arrays.toString( parameters ) );
        }

        RequestBuilder requestBuilder = new RequestBuilder( "GET" ).setUrl( uri.toString() );

        for ( Parameter parameter : parameters )
        {
            requestBuilder.addQueryParameter( parameter.getName(), parameter.getValue() );
        }

        if ( realms.containsKey( uri.getHost() ) )
        {
            requestBuilder.setRealm( realms.get( uri.getHost() ) );
        }

        CE description;

        try
        {
            description = httpClient.executeRequest( requestBuilder.build(), new AsyncCompletionHandler<CE>()
            {

                @Override
                public CE onCompleted( Response response )
                    throws Exception
                {
                    return digesterLoader.newDigester().parse( response.getResponseBodyAsStream() );
                }

            } ).get();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "An error occurred while invoking " + uri, e );
        }

        description.setCatalogueClient( this );
        return description;
    }

    void downloadFile( File targetDir, List<URI> fileUris, final DownloadHandler handler )
    {
        if ( !targetDir.exists() )
        {
            if ( logger.isInfoEnabled() )
            {
                logger.info( "Directory {} does not exist, creating it...", targetDir );
            }

            if ( !targetDir.mkdirs() )
            {
                throw new RuntimeException( format( "Impossible to create '%s' directory, please make sure you have enough permissions",
                                                    targetDir ) );
            }
            else if ( logger.isInfoEnabled() )
            {
                logger.info( "Directory {} created.", targetDir );
            }
        }

        CallbackDownloadHandler callback = new CallbackDownloadHandler( handler );
        Iterator<URI> fileUrisIterator = fileUris.iterator();

        while ( !callback.isDownloaded() )
        {
            if ( !fileUrisIterator.hasNext() )
            {
                callback.onFatal( "DataSet media file download not possible, none of the submitted URIs succeeded" );

                return;
            }

            URI fileUri = fileUrisIterator.next();

            Downloader downloader = lookupDownloader( fileUri.getScheme() );

            if ( downloader != null )
            {
                downloader.download( targetDir, fileUri, callback );
            }
            else
            {
                callback.onWarning( format( "'%s' protocol is not supported, impossible to download %s",
                                            fileUri.getScheme(), fileUri ) );
            }
        }
    }

    public void registerRealm( String host, String username, String password, boolean preemptive, HttpAuthScheme authScheme )
    {
        host = checkNotNull( host, "host cannot be null" );
        username = checkNotNull( username, "username cannot be null" );
        password = checkNotNull( password, "password cannot be null" );
        authScheme = checkNotNull( authScheme, "authScheme cannot be null" );

        realms.put( host, new Realm.RealmBuilder()
                                   .setPrincipal( username )
                                   .setPassword( password )
                                   .setUsePreemptiveAuth( preemptive )
                                   .setScheme( authScheme.getAuthScheme() )
                                   .build() );
    }

    public void registerSSLProxy( File proxyCertificate )
    {
        registerSSLCerificates( proxyCertificate, proxyCertificate, null );
    }

    public void registerSSLCerificates( File sslCertificate, File sslKey, String sslPassword )
    {
        checkFile( sslCertificate );
        checkFile( sslKey );

        if ( logger.isInfoEnabled() )
        {
            logger.info( "Registering SSL certificate {} and key {} with password {}",
                         new Object[] { sslCertificate, sslKey, encrypt( sslPassword ) } );
        }

        if ( sslPassword == null )
        {
            sslPassword = "";
        }

        final char[] password = sslPassword.toCharArray();

        try
        {
            final KeyStore store = new KeyMaterial( sslCertificate, sslKey, password ).getKeyStore();
            store.load( null, password );

            // initialize key and trust managers -> default behavior
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance( "SunX509" );
            // password for key and store have to be the same IIRC
            keyManagerFactory.init( store, password );

            keyManagers.addAll( asList( keyManagerFactory.getKeyManagers() ) );

            httpClient.getConfig().getSSLContext().init( keyManagers.toArray( new KeyManager[keyManagers.size()] ),
                                                         trustManagers,
                                                         null );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "Impossible to initialize SSL certificate/key", e );
        }
    }

    private static String encrypt( String pwd )
    {
        StringBuilder sBuilder = new StringBuilder();

        if ( pwd != null )
        {
            for ( int i = 0; i < pwd.length(); i++ )
            {
                sBuilder.append( '*' );
            }
        }

        return sBuilder.toString();
    }

    /**
     * @since 0.8
     */
    public void registerUmSsoCredentials( String host, String username, String password )
    {
        host = checkNotNull( host, "host cannot be null" );
        username = checkNotNull( username, "username cannot be null" );
        password = checkNotNull( password, "password cannot be null" );

        umSsoCredentials.put( host, new Credentials( username, password ) );
    }

    /**
     * @since 0.8
     */
    public void shutDown()
    {
        httpClient.close();
    }

}
