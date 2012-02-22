package com.terradue.jcatalogue.client.download;

import java.io.File;
import java.net.URI;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Realm;
import com.ning.http.client.RequestBuilder;
import com.terradue.jcatalogue.client.HttpMethod;

@Data
@Protocol({ "http", "https" })
public final class HttpDownloader
    implements Downloader
{

    private final Logger logger = LoggerFactory.getLogger( getClass() );

    @Getter( AccessLevel.NONE )
    private final AsyncHttpClient httpClient;

    @Getter( AccessLevel.NONE )
    private final Map<String, Realm> realms;

    /**
     * @deprecated
     */
    @Deprecated
    public void registerRealm( String host, Realm realm )
    {
        throw new UnsupportedOperationException( "Since 0.7 this method is no longer supported" );
    }

    @Override
    public void download( File targetDir, URI fileUri, DownloadHandler handler )
    {
        String fileName = fileUri.getPath().substring( fileUri.getPath().lastIndexOf( '/' ) + 1 );

        File targetFile = new File( targetDir, fileName );

        if ( logger.isInfoEnabled() )
        {
            logger.info( "Downloading {} to {}...", fileUri, targetDir );
        }

        RequestBuilder requestBuilder = new RequestBuilder( HttpMethod.GET.toString() ).setUrl( fileUri.toString() );

        if ( realms.containsKey( fileUri.getHost() ) )
        {
            requestBuilder.setRealm( realms.get( fileUri.getHost() ) );
        }

        try
        {
            httpClient.executeRequest( requestBuilder.build(), new SimpleDownloadHandler( targetFile, handler ) ).get();
        }
        catch ( Exception e )
        {
            handler.onError( e );
        }
    }

}
