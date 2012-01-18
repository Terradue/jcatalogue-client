package com.terradue.jcatalogue.client.download;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

    private final File targetFile;

    private final FileOutputStream output;

    private final DownloadHandler downloadHandler;

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
        return STATE.CONTINUE;
    }

    @Override
    public Void onCompleted()
        throws Exception
    {
        output.flush();
        output.close();

        downloadHandler.onSuccess( targetFile );
        return null;
    }

}
