package com.terradue.jcatalogue.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import com.terradue.jcatalogue.client.download.DownloadHandler;

final class AssertingDownloadHanlder
    implements DownloadHandler<Void>
{

    @Override
    public void onContentDownloadProgress( long current, long total )
    {
        System.out.printf( "%s%%\r", ( ( 100 * current ) / total ) );
    }

    public Void onCompleted( File file )
    {
        System.out.println( "Done!" );
        assertTrue( file.exists() );
        return null;
    }

    @Override
    public void onError( String message )
    {
        fail( message );
    }

    @Override
    public void onWarning( String message )
    {
        System.out.printf( "[WARNING] %s%n",  message );
    }

    @Override
    public void onFatal( String message )
    {
        fail( message );
    }

    @Override
    public void onError( Throwable t )
    {
        fail( t.getMessage() );
    }

}
