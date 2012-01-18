package com.terradue.jcatalogue.client.download;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;

/**
 * @since 0.5
 */
final class SimpleDownloadHanlder
    implements AsyncHandler<Void>
{

    private static final String CONTENT_LENGTH = "Content-Length";

    private final File targetFile;

    private final FileOutputStream output;

    private final DownloadHandler downloadHandler;

    private int contentLength = -1;

    private int current = 0;

    public SimpleDownloadHanlder( File targetFile, DownloadHandler downloadHandler )
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
            current += bodyPart.getBodyPartBytes().length;

            System.out.print( ( ( 100 * current ) / contentLength ) + "%\r" );
        }

        return STATE.CONTINUE;
    }

    @Override
    public STATE onStatusReceived( HttpResponseStatus responseStatus )
        throws Exception
    {
        if ( HTTP_OK != responseStatus.getStatusCode() )
        {
            downloadHandler.onError( format( "Impossible to download %s from %s, server replied %s",
                                     targetFile.getName(), targetFile.getName(), responseStatus.getStatusText() ) ) ;
            targetFile.delete();

            return STATE.ABORT;
        }
        return STATE.CONTINUE;
    }

    @Override
    public STATE onHeadersReceived( HttpResponseHeaders headers )
        throws Exception
    {
        List<String> contentLength = headers.getHeaders().get( CONTENT_LENGTH );

        if ( !contentLength.isEmpty() )
        {
            this.contentLength = Integer.valueOf( contentLength.iterator().next() );
        }

        return STATE.CONTINUE;
    }

    @Override
    public Void onCompleted()
        throws Exception
    {
        System.out.println( "Done." );

        output.flush();
        output.close();

        downloadHandler.onSuccess( targetFile );
        return null;
    }

}
