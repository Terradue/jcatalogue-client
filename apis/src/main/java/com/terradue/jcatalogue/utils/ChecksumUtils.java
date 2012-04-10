package com.terradue.jcatalogue.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A utility class to assist in the verification and generation of checksums.
 */
public final class ChecksumUtils
{

    private static Charset UTF_8 = Charset.forName( "UTF-8" );

    private ChecksumUtils()
    {
        // hide constructor
    }

    public static String read( InputStream input )
        throws IOException
    {
        String checksum = "";

        try
        {
            BufferedReader br = new BufferedReader( new InputStreamReader( input, UTF_8 ) );
            try
            {
                String line = null;
                while ( ( line = br.readLine() ) != null )
                {
                    line = line.trim();
                    if ( line.length() > 0 )
                    {
                        checksum = line;
                        break;
                    }
                }
            }
            finally
            {
                closeQuietly( br );
            }
        }
        finally
        {
            closeQuietly( input );
        }

        if ( checksum.matches( ".+= [0-9A-Fa-f]+" ) )
        {
            int lastSpacePos = checksum.lastIndexOf( ' ' );
            checksum = checksum.substring( lastSpacePos + 1 );
        }
        else
        {
            int spacePos = checksum.indexOf( ' ' );

            if ( spacePos != -1 )
            {
                checksum = checksum.substring( 0, spacePos );
            }
        }

        return checksum;
    }

    /**
     * Calculates checksums for the specified file.
     *
     * @param dataFile The file for which to calculate checksums, must not be {@code null}.
     * @param algos The names of checksum algorithms (cf. {@link MessageDigest#getInstance(String)} to use, must not be
     *            {@code null}.
     * @return The calculated checksums, indexed by algorithm name, or the exception that occurred while trying to
     *         calculate it, never {@code null}.
     * @throws IOException If the data file could not be read.
     */
    public static Map<String, String> calc( File dataFile, String...algos )
        throws IOException
    {
        Map<String, String> results = new LinkedHashMap<String, String>();

        Map<String, MessageDigest> digests = new LinkedHashMap<String, MessageDigest>();
        for ( String algo : algos )
        {
            try
            {
                digests.put( algo, MessageDigest.getInstance( algo ) );
            }
            catch ( NoSuchAlgorithmException e )
            {
                results.put( algo, null );
            }
        }

        FileInputStream fis = new FileInputStream( dataFile );
        try
        {
            for ( byte[] buffer = new byte[32 * 1024];; )
            {
                int read = fis.read( buffer );
                if ( read < 0 )
                {
                    break;
                }
                for ( MessageDigest digest : digests.values() )
                {
                    digest.update( buffer, 0, read );
                }
            }
        }
        finally
        {
            closeQuietly( fis );
        }

        for ( Map.Entry<String, MessageDigest> entry : digests.entrySet() )
        {
            byte[] bytes = entry.getValue().digest();

            results.put( entry.getKey(), toHexString( bytes ) );
        }

        return results;
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
                // ignored
            }
        }
    }

    private static String toHexString( byte[] bytes )
    {
        StringBuilder buffer = new StringBuilder( bytes.length * 2 );

        for ( int i = 0; i < bytes.length; i++ )
        {
            int b = bytes[i] & 0xFF;
            if ( b < 0x10 )
            {
                buffer.append( '0' );
            }
            buffer.append( Integer.toHexString( b ) );
        }

        return buffer.toString();
    }

}
