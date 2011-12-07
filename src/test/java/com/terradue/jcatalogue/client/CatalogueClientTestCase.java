package com.terradue.jcatalogue.client;

/*
 *    Copyright 2011 Terradue srl
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ning.http.client.Realm;
import com.ning.http.client.Realm.AuthScheme;
import com.terradue.jcatalogue.client.download.DownloadHandler;
import com.terradue.jcatalogue.client.download.HttpDownloader;

public final class CatalogueClientTestCase
{

    private CatalogueClient client;

    private String username = "";

    private String password = "";

    @Before
    public void setUp()
    {
        client = new CatalogueClient();

        HttpDownloader httpDownloader = client.lookupDownloader( "http" );
        httpDownloader.registerRealm( "eo-virtual-archive4.esa.int",
                                      new Realm.RealmBuilder()
                                            .setPrincipal( username )
                                            .setPassword( password )
                                            .setUsePreemptiveAuth( true )
                                            .setScheme( AuthScheme.BASIC )
                                        .build() );
    }

    @After
    public void tearDown()
    {
        client = null;
    }

    @Test
    public void discoverCatalogDescription()
        throws Exception
    {
        CatalogueDescription catalogueDescription = client.discover( "http://eo-virtual-archive4.esa.int/search/description" );

        assertNotNull( catalogueDescription );
    }

    @Test
    public void accessToCatalogViaDescription()
        throws Exception
    {
        CatalogueDescription catalogueDescription = client.discover( "http://eo-virtual-archive4.esa.int/search/description" );
        Catalogue catalogue = catalogueDescription.getCatalogue();

        assertNotNull( catalogue );
    }

    @Test
    public void cannotAccessToCatalogPagination()
    {
        CatalogueDescription catalogueDescription = client.discover( "http://eo-virtual-archive4.esa.int/search/description" );
        Catalogue catalogue = catalogueDescription.getCatalogue();

        assertFalse( catalogue.hasMoreResults() );
    }

    @Test
    @Ignore
    public void traverseCatalogue()
    {
        // get the catalogue

        CatalogueDescription catalogueDescription = client.discover( "http://eo-virtual-archive4.esa.int/search/description" );
        Catalogue catalogue = catalogueDescription.getCatalogue();

        // series

        Iterator<Serie> seriesIterator = catalogue.iterator();

        assertTrue( seriesIterator.hasNext() );

        Serie serie = seriesIterator.next();

        assertNotNull( serie );

        // dataset

        Iterator<DataSet> dataSetIterator = serie.iterator();

        assertTrue( dataSetIterator.hasNext() );

        DataSet dataSet = dataSetIterator.next();

        assertNotNull( dataSet );

        // dowload the DataSet

        dataSet.download( new File( "/tmp" ), new DownloadHandler()
        {

            @Override
            public void onSuccess( File file )
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

        } );
    }

    /**
     * @since 0.2
     */
    @Test
    public void accessToDataSet()
        throws Exception
    {
        DataSet dataSet = client.getDataSet( "http://10.11.12.248/catalogue/gpod/ERS2_WILMA/AMI_WILM__0P_19950718T144654_19950718T144710_KS_01267.E2/atom" );

        System.out.println( dataSet );

        assertNotNull( dataSet );
    }

}
