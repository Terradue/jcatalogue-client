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

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.apache.commons.beanutils.ConvertUtils.register;
import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.digester3.binder.DigesterLoader;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import com.ning.http.client.resumable.ResumableIOExceptionFilter;
import com.terradue.jcatalogue.client.converters.AtomDateConverter;
import com.terradue.jcatalogue.client.converters.CharsetConverter;
import com.terradue.jcatalogue.client.converters.LocaleConverter;
import com.terradue.jcatalogue.client.digester.AtomRulesModule;
import com.terradue.jcatalogue.client.digester.DataSetRulesModule;
import com.terradue.jcatalogue.client.digester.LinkedAtomEntityModule;
import com.terradue.jcatalogue.client.digester.OpenSearchModule;
import com.terradue.jcatalogue.client.download.Downloader;
import com.terradue.jcatalogue.client.download.Protocol;

public final class CatalogueClient
{

    static
    {
        register( new AtomDateConverter(), Date.class );
        register( new LocaleConverter(), Locale.class );
        register( new CharsetConverter(), Charset.class );
    }

    private final Map<String, Downloader> downloaders = new HashMap<String, Downloader>();

    private final DigesterLoader descriptionDigesterLoader;

    private final DigesterLoader catalogueDigesterLoader;

    private final DigesterLoader serieDigesterLoader;

    private final AsyncHttpClient httpClient;

    public CatalogueClient()
    {
        descriptionDigesterLoader = newLoader( new OpenSearchModule() );
        catalogueDigesterLoader = newLoader( new AtomRulesModule( Catalogue.class ), new LinkedAtomEntityModule() );
        serieDigesterLoader = newLoader( new AtomRulesModule( Serie.class ), new DataSetRulesModule() );
        httpClient = new AsyncHttpClient(new AsyncHttpClientConfig.Builder()
                            .setAllowPoolingConnection( true )
                            .addIOExceptionFilter( new ResumableIOExceptionFilter() )
                            .setMaximumConnectionsPerHost( 10 )
                            .setMaximumConnectionsTotal( 100 )
                            .setFollowRedirects( true )
                            .build() );
    }

    public void registerDownloader( Downloader downloader )
    {
        if ( downloader == null )
        {
            throw new IllegalArgumentException( "Input downloader must not be null." );
        }

        if ( !downloader.getClass().isAnnotationPresent( Protocol.class ) )
        {
            throw new RuntimeException( format( "Class %s must be annotated with %s",
                                                downloader.getClass().getName(),
                                                Protocol.class.getName() ) );
        }

        for ( String protocol : downloader.getClass().getAnnotation( Protocol.class ).value() )
        {
            registerDownloader( protocol, downloader );
        }
    }

    public void registerDownloader( String protocol, Downloader downloader )
    {
        if ( protocol == null )
        {
            throw new IllegalArgumentException( "Input protocol must not be null." );
        }
        if ( downloader == null )
        {
            throw new IllegalArgumentException( "Input downloader must not be null." );
        }

        downloaders.put( protocol, downloader );
    }

    public <D extends Downloader> D lookupDownloader( String protocol )
    {
        if ( protocol == null )
        {
            throw new IllegalArgumentException( "Input protocol must be not null" );
        }

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

    // Serie methods

    public Serie getSerie( String uri, Parameter... parameters )
    {
        return invoke( serieDigesterLoader, uri, parameters );
    }

    public Serie getSerie( URI uri, Parameter... parameters )
    {
        return invoke( serieDigesterLoader, uri, parameters );
    }

    // generic internal methods

    <CE extends CatalogueEntity> CE invoke( final DigesterLoader digesterLoader, String uri, Parameter...parameters )
    {
        if ( uri == null )
        {
            throw new IllegalArgumentException( "Input URI must be not null" );
        }

        try
        {
            return invoke( digesterLoader, new URI( uri ), parameters );
        }
        catch ( URISyntaxException e )
        {
            throw new RuntimeException( uri + " is not a valid URI", e );
        }
    }

    <CE extends CatalogueEntity> CE invoke( final DigesterLoader digesterLoader, URI uri, Parameter...parameters )
    {
        if ( uri == null )
        {
            throw new IllegalArgumentException( "Input URI must be not null" );
        }

        CE description;

        try
        {
            description = httpClient.prepareGet( uri.toString() ).execute( new AsyncCompletionHandler<CE>()
            {

                @Override
                public CE onCompleted( Response response )
                    throws Exception
                {
                    if ( HTTP_OK != response.getStatusCode() )
                    {
                        throw new IllegalStateException( format( "Impossible to query the catalog, server replied %s",
                                                                 response.getStatusText() ) );
                    }
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

    void downloadFile( File targetDir, String fileUrl )
    {
        if ( !targetDir.exists() )
        {
            if ( !targetDir.mkdirs() )
            {
                throw new RuntimeException( format( "Impossible to create '%s' directory, please make sure you have enough permissions",
                                                    targetDir ) );
            }
        }

        String protocol = fileUrl.substring( 0, fileUrl.indexOf( ':' ) );

        Downloader downloader = lookupDownloader( protocol );

        try
        {
            downloader.download( targetDir, fileUrl );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( format( "Impossible to download file %s under directory %s",
                                                  fileUrl, targetDir ),
                                          e );
        }
    }

}
