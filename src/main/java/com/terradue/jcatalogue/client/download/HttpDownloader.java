package com.terradue.jcatalogue.client.download;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Realm;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import com.ning.http.client.extra.ResumableRandomAccessFileListener;
import com.ning.http.client.resumable.ResumableAsyncHandler;

@Data
@Protocol({ "http", "https" })
public final class HttpDownloader
    implements Downloader
{

    private static final String RW = "rw";

    private static final String GET = "GET";

    @Getter( AccessLevel.NONE )
    private final AsyncHttpClient httpClient;

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

        ResumableAsyncHandler<Response> resumableHandler = new ResumableAsyncHandler<Response>();
        try
        {
            resumableHandler.setResumableListener( new ResumableRandomAccessFileListener( new RandomAccessFile( targetFile,
                                                                                                                RW ) ) );
        }
        catch ( FileNotFoundException e )
        {
            // TODO verify, but that should not happen
        }

        RequestBuilder requestBuilder = new RequestBuilder( GET ).setUrl( fileUri.toString() );

        if ( realms.containsKey( fileUri.getHost() ) )
        {
            requestBuilder.setRealm( realms.get( fileUri.getHost() ) );
        }

        Response response;
        try
        {
            response = httpClient.executeRequest( requestBuilder.build(), resumableHandler ).get();

            if ( HTTP_OK != response.getStatusCode() )
            {
                handler.onError( format( "Impossible to download file %s, server replied %s",
                                         fileUri, response.getStatusText() ) ) ;
            }

            handler.onSuccess( targetFile );
        }
        catch ( Exception e )
        {
            handler.onError( e );
        }
    }

}
