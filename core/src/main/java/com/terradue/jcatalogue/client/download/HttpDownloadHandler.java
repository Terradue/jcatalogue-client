package com.terradue.jcatalogue.client.download;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;

/**
 * @since 0.5
 */
final class HttpDownloadHandler<T>
    implements AsyncHandler<T>
{

    private static final String CONTENT_LENGTH = "Content-Length";

    private final File targetFile;

    private final FileOutputStream output;

    private final DownloadHandler<T> downloadHandler;

    private long contentLength = -1;

    private long downloadCounter = 0;

    public HttpDownloadHandler( File targetFile, DownloadHandler<T> downloadHandler )
        throws FileNotFoundException
    {
        this.targetFile = targetFile;
        output = new FileOutputStream( targetFile );
        this.downloadHandler = downloadHandler;
    }

    @Override
    public void onThrowable( Throwable t )
    {
        downloadHandler.onError( t );
    }

    @Override
    public STATE onBodyPartReceived( HttpResponseBodyPart bodyPart )
        throws Exception
    {
        bodyPart.writeTo( output );

        // print the percentage progress on shell
        if ( contentLength > 0 )
        {
            downloadCounter += bodyPart.getBodyPartBytes().length;

            downloadHandler.onContentDownloadProgress( downloadCounter, contentLength );
        }

        return STATE.CONTINUE;
    }

    @Override
    public STATE onStatusReceived( HttpResponseStatus responseStatus )
        throws Exception
    {
        return STATE.CONTINUE;
    }

    @Override
    public STATE onHeadersReceived( HttpResponseHeaders headers )
        throws Exception
    {
        List<String> contentLength = headers.getHeaders().get( CONTENT_LENGTH );

        if ( !contentLength.isEmpty() )
        {
            this.contentLength = Long.valueOf( contentLength.iterator().next() ).longValue();
        }

        return STATE.CONTINUE;
    }

    @Override
    public T onCompleted()
        throws Exception
    {
        output.flush();
        closeQuietly( output );

        return downloadHandler.onCompleted( targetFile );
    }

    private static void closeQuietly( Closeable closeable )
    {
        if ( closeable != null )
        {
            try
            {
                closeable.close();
            }
            catch ( IOException e )
            {
                // close quietly
            }
        }
    }

}
