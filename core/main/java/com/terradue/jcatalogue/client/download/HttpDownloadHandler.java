package com.terradue.jcatalogue.client.download;

import static java.nio.charset.Charset.forName;
import static java.lang.String.format;
import static java.security.MessageDigest.getInstance;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /**
     * Used to build output as Hex
     */
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static final Charset UTF_8 = forName( "UTF-8" );

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final String MD5 = "MD5";

    private final File targetFile;

    private final FileOutputStream output;

    private final DownloadHandler<T> downloadHandler;

    private final MessageDigest md5Digest;

    private long contentLength = -1;

    private long downloadCounter = 0;

    public HttpDownloadHandler( File targetFile, DownloadHandler<T> downloadHandler )
        throws FileNotFoundException
    {
        this.targetFile = targetFile;
        output = new FileOutputStream( targetFile );
        this.downloadHandler = downloadHandler;

        try
        {
            md5Digest = getInstance( MD5 );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // should not happen
            throw new RuntimeException( e.getMessage() );
        }
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
        md5Digest.update( bodyPart.getBodyPartBytes() );

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

        writeMd5ChecksumFile();

        return downloadHandler.onCompleted( targetFile );
    }

    private void writeMd5ChecksumFile()
    {
        File checksumFile = new File( targetFile.getParent(), format( "%.md5", targetFile.getName() ) );
        try
        {
            FileChannel digestChannel = new FileOutputStream( checksumFile ).getChannel();

            byte[] checksumData = md5Digest.digest();
            char[] hexData = new char[checksumData.length << 1];

            for ( int i = 0, j = 0; i < checksumData.length; i++ )
            {
                hexData[j++] = HEX_DIGITS[( 0xF0 & hexData[i] ) >>> 4];
                hexData[j++] = HEX_DIGITS[0x0F & hexData[i]];
            }

            digestChannel.write( ByteBuffer.wrap( new String( hexData ).getBytes( UTF_8 ) ) );

            digestChannel.close();
        }
        catch ( Exception e )
        {
            // simply skip the .md5 file generation
        }
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
