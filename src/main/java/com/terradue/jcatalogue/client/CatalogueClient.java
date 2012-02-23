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
import static org.apache.commons.beanutils.ConvertUtils.register;
import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import com.terradue.jcatalogue.client.download.DownloadHandler;
import com.terradue.jcatalogue.client.download.Downloader;
import com.terradue.jcatalogue.client.download.HttpDownloader;
import com.terradue.jcatalogue.client.download.Protocol;
import com.terradue.jcatalogue.client.geo.Line;
import com.terradue.jcatalogue.client.geo.Point;
import com.terradue.jcatalogue.client.geo.Polygon;
import com.terradue.jcatalogue.client.internal.ahc.HttpInvoker;
import com.terradue.jcatalogue.client.internal.converters.AtomDateConverter;
import com.terradue.jcatalogue.client.internal.converters.CharsetConverter;
import com.terradue.jcatalogue.client.internal.converters.GeoConverter;
import com.terradue.jcatalogue.client.internal.converters.LocaleConverter;
import com.terradue.jcatalogue.client.internal.digester.AtomRulesModule;
import com.terradue.jcatalogue.client.internal.digester.DataSetRulesModule;
import com.terradue.jcatalogue.client.internal.digester.LinkedAtomEntityModule;
import com.terradue.jcatalogue.client.internal.digester.OpenSearchModule;
import com.terradue.jcatalogue.client.internal.digester.SingleDataSetRulesModule;

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

    @Getter
    private final HttpInvoker httpInvoker = new HttpInvoker();

    private final DigesterLoader descriptionDigesterLoader;

    private final DigesterLoader catalogueDigesterLoader;

    private final DigesterLoader serieDigesterLoader;

    private final DigesterLoader singleDataSetDigesterLoader;

    public CatalogueClient()
    {
        descriptionDigesterLoader = newLoader( new OpenSearchModule() ).setNamespaceAware( true );
        catalogueDigesterLoader = newLoader( new AtomRulesModule( Catalogue.class ), new LinkedAtomEntityModule() )
            .setNamespaceAware( true );
        serieDigesterLoader = newLoader( new AtomRulesModule( Series.class ), new DataSetRulesModule() ).setNamespaceAware( true );
        singleDataSetDigesterLoader = newLoader( new SingleDataSetRulesModule() ).setNamespaceAware( true );

        registerDownloader( new HttpDownloader( httpInvoker ) );
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

        CE description = httpInvoker.invoke( HttpMethod.GET, uri, new AsyncCompletionHandler<CE>()
        {

            @Override
            public CE onCompleted( Response response )
                throws Exception
            {
                return digesterLoader.newDigester().parse( response.getResponseBodyAsStream() );
            }

        }, parameters );

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

    /**
     * @since 0.7
     */
    public void registerRealm( String host, String username, String password, boolean preemptive, HttpAuthScheme authScheme )
    {
        httpInvoker.registerRealm( host, username, password, preemptive, authScheme );
    }

    /**
     * @since 0.7
     */
    public void registerSSLProxy( File proxyCertificate )
    {
        httpInvoker.registerSSLProxy( proxyCertificate );
    }

    /**
     * @since 0.7
     */
    public void registerSSLCerificates( File sslCertificate, File sslKey, String sslPassword )
    {
        httpInvoker.registerSSLCerificates( sslCertificate, sslKey, sslPassword );
    }

    /**
     * @since 0.8
     */
    public void registerUmSsoAccess( String loginFormUrl, HttpMethod httpMethod, Parameter...parameters )
    {
        httpInvoker.registerUmSsoAccess( loginFormUrl, httpMethod, parameters );
    }

    /**
     * @since 0.8
     */
    public void registerUmSsoCredentials( URI loginFormUrl, HttpMethod httpMethod, Parameter...parameters )
    {
        httpInvoker.registerUmSsoCredentials( loginFormUrl, httpMethod, parameters );
    }

    /**
     * @since 0.8
     */
    public void shutDown()
    {
        httpInvoker.shutDown();
    }

}
