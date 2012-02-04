package com.terradue.jcatalogue.client.download;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Realm;
import com.ning.http.client.RequestBuilder;

@Data
@Protocol({ "http", "https" })
public final class HttpDownloader
    implements Downloader
{

    private static final String GET = "GET";

    @Getter( AccessLevel.NONE )
    private final AsyncHttpClient httpClient;

    private final Logger logger = LoggerFactory.getLogger( getClass() );

    private final Map<String, Realm> realms = new HashMap<String, Realm>();

    public void registerRealm( String host, Realm realm )
    {
        if ( host == null )
        {
            throw new IllegalArgumentException( "Input host must be not null" );
        }
        if ( realm == null )
        {
            throw new IllegalArgumentException( "Input realm must be not null" );
        }

        realms.put( host, realm );
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

        RequestBuilder requestBuilder = new RequestBuilder( GET ).setUrl( fileUri.toString() );

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
