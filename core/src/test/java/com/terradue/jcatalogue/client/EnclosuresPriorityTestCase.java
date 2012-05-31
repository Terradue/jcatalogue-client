package com.terradue.jcatalogue.client;

/*
 *    Copyright 2011-2012 Terradue srl
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import java.io.File;
import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.terradue.jcatalogue.client.download.DownloadHandler;
import com.terradue.jcatalogue.client.download.Downloader;

public final class EnclosuresPriorityTestCase
{

    private CatalogueClient client;

    @Before
    public void setUp()
    {
        client = new CatalogueClient( new CatalogueClient.Configuration().registerDownloader( "http", new Downloader()
        {

            @Override
            public <T> T download( File targetDir, URI fileUri, DownloadHandler<T> handler )
            {
                // do nothing, just print out the uri
                System.out.println( "******************************************** >> " + fileUri );
                return null;
            }

        } ) );
    }

    @After
    public void tearDown()
    {
        client.shutDown();
        client = null;
    }

    @Test
    public void accessToSeriesWhereEachDataSetEnclosureHasPriority()
    {
        Series series = client.getSeries( "http://10.11.12.248/catalogue/gpod/ASA_APH_0P/ASA_APH_0CNPDK20050612_135118_000000162038_00082_17170_0011.N1/atom" );
        for ( DataSet dataSet : series )
        {
            dataSet.download( new File( "/tmp" ), new DownloadHandler<Void>()
            {

                @Override
                public void onError( Throwable t )
                {
                    // do nothing
                }

                @Override
                public void onError( String message )
                {
                    // do nothing
                }

                @Override
                public void onWarning( String message )
                {
                    // do nothing
                }

                @Override
                public void onFatal( String message )
                {
                    // do nothing
                }

                @Override
                public void onContentDownloadProgress( long current, long total )
                {
                    // do nothing
                }

                @Override
                public Void onCompleted( File file )
                {
                    // do nothing
                    return null;
                }

            } );
        }
    }

}
