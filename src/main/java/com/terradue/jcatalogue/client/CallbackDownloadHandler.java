package com.terradue.jcatalogue.client;

import java.io.File;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.terradue.jcatalogue.client.download.DownloadHandler;

@RequiredArgsConstructor
final class CallbackDownloadHandler
    implements DownloadHandler
{

    private final DownloadHandler adapted;

    @Getter
    private boolean downloaded = false;

    @Override
    public void onError( Throwable t )
    {
        adapted.onError( t );
    }

    @Override
    public void onError( String message )
    {
        adapted.onError( message );
    }

    @Override
    public void onWarning( String message )
    {
        adapted.onWarning( message );
    }

    @Override
    public void onFatal( String message )
    {
        adapted.onFatal( message );
    }

    @Override
    public void onSuccess( File file )
    {
        downloaded = true;
        adapted.onSuccess( file );
    }

}
