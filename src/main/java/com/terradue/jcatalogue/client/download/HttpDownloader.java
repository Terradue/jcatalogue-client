package com.terradue.jcatalogue.client.download;

import java.io.File;
import java.net.URI;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terradue.jcatalogue.client.HttpMethod;
import com.terradue.jcatalogue.client.internal.ahc.HttpInvoker;

@Data
@Protocol({ "http", "https" })
public final class HttpDownloader
    implements Downloader
{

    private final Logger logger = LoggerFactory.getLogger( getClass() );

    @Getter( AccessLevel.NONE )
    private final HttpInvoker httpInvoker;

    @Override
    public void download( File targetDir, URI fileUri, DownloadHandler handler )
    {
        String fileName = fileUri.getPath().substring( fileUri.getPath().lastIndexOf( '/' ) + 1 );

        File targetFile = new File( targetDir, fileName );

        if ( logger.isInfoEnabled() )
        {
            logger.info( "Downloading {} to {}...", fileUri, targetDir );
        }

        try
        {
            httpInvoker.invoke( HttpMethod.GET, fileUri, new SimpleDownloadHandler( targetFile, handler ) );
        }
        catch ( Exception e )
        {
            handler.onError( e );
        }
    }

}
