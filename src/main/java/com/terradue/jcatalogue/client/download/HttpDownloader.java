package com.terradue.jcatalogue.client.download;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.File;
import java.io.RandomAccessFile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.client.extra.ResumableRandomAccessFileListener;
import com.ning.http.client.resumable.ResumableAsyncHandler;

@Data
@Protocol({ "http", "https" })
public final class HttpDownloader
    implements Downloader
{

    private static final String RW = "rw";

    @Getter( AccessLevel.NONE )
    private final AsyncHttpClient httpClient;

    @Override
    public void download( File targetDir, String fileUrl )
        throws Exception
    {
        String fileName = fileUrl.substring( fileUrl.lastIndexOf( '/' ) + 1 );

        ResumableAsyncHandler<Response> resumableHandler = new ResumableAsyncHandler<Response>();
        resumableHandler.setResumableListener( new ResumableRandomAccessFileListener( new RandomAccessFile( fileName,
                                                                                                            RW ) ) );
        Response response = httpClient.prepareGet( fileUrl ).execute( resumableHandler ).get();

        if ( HTTP_OK != response.getStatusCode() )
        {
            throw new Exception( format( "Impossible to download file %s, server replied %s",
                                         fileUrl, response.getStatusText() ) );
        }
    }

}
