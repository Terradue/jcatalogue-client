package com.terradue.jcatalogue.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import com.terradue.jcatalogue.client.download.DownloadHandler;

final class AssertingDownloadHanlder
    implements DownloadHandler
{

    public void onCompleted( File file )
    {
        assertTrue( file.exists() );
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
