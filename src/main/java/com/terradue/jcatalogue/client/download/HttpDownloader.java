package com.terradue.jcatalogue.client.download;

import java.io.File;

import org.kohsuke.MetaInfServices;

@MetaInfServices
@Protocol({ "http", "https" })
final class HttpDownloader
    implements Downloader
{

    @Override
    public void download( File targetDir, String fileUrl )
        throws Exception
    {
        // TODO
    }

}
