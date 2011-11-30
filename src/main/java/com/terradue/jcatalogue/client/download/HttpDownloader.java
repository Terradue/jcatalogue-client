package com.terradue.jcatalogue.client.download;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.File;
import java.io.RandomAccessFile;
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
    public void download( File targetDir, String fileUrl )
        throws Exception
    {
        String fileName = fileUrl.substring( fileUrl.lastIndexOf( '/' ) + 1 );

        ResumableAsyncHandler<Response> resumableHandler = new ResumableAsyncHandler<Response>();
        resumableHandler.setResumableListener( new ResumableRandomAccessFileListener( new RandomAccessFile( fileName,
                                                                                                            RW ) ) );

        RequestBuilder requestBuilder = new RequestBuilder( GET ).setFollowRedirects( true );

        Response response = httpClient.executeRequest( requestBuilder.build(), resumableHandler ).get();

        if ( HTTP_OK != response.getStatusCode() )
        {
            throw new Exception( format( "Impossible to download file %s, server replied %s",
                                         fileUrl, response.getStatusText() ) );
        }
    }

}
