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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.terradue.jcatalogue.client.download.DownloadHandler;

public final class CatalogueClientTestCase
{

    private CatalogueClient client;

    private String username = "";

    private String password = "";

    @Before
    public void setUp()
    {
        client = new CatalogueClient();
        client.registerRealm( "eo-virtual-archive4.esa.int", username, password, true, HttpAuthScheme.BASIC );
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

        Iterator<Series> seriesIterator = catalogue.iterator();

        assertTrue( seriesIterator.hasNext() );

        Series serie = seriesIterator.next();

        assertNotNull( serie );

        // dataset

        Iterator<DataSet> dataSetIterator = serie.iterator();

        assertTrue( dataSetIterator.hasNext() );

        DataSet dataSet = dataSetIterator.next();

        assertNotNull( dataSet );

        // dowload the DataSet

        dataSet.download( new File( "/tmp" ), new AssertingDownloadHanlder() );
    }

    /**
     * @since 0.2
     */
    @Test
    public void accessToDataSet()
        throws Exception
    {
        DataSet dataSet = client.getDataSet( "http://t2-10-11-12-248.hadoop.terradue.int/catalogue/gpod/ASA_IM__0P/ASA_IM__0CNPDE20110409_004512_000000163101_00189_47617_2648.N1/atom" );

        assertNotNull( dataSet );
    }

    /**
     * @since 0.3
     */
    @Test
    public void testQueryParameters()
        throws Exception
    {
        Series serie = client.getSeries( "http://10.11.12.248/catalogue/gpod/ER2_TIM_AX/atom",
                                         new Parameter( "startDate", "1995-07-18T14:46:54.000" ),
                                         new Parameter( "stopDate", "1995-07-18T14:46:54.000" ) );

        assertTrue( serie.getTotalResults() > 0 );
    }

    /**
     * @since 0.8
     */
    @Test
    public void ssoLogin()
        throws Exception
    {
        client.registerUmSsoCredentials( URI.create( "https://eo-sso-idp.eo.esa.int/idp/umsso20/login?null" ),
                                         HttpMethod.POST,
                                         new Parameter( "cn", "stripodi" ),
                                         new Parameter( "password", "XXX" ),
                                         new Parameter( "idleTime", "oneday" ),
                                         new Parameter( "sessionTime", "untilbrowserclose" ),
                                         new Parameter( "loginFields", "cn@password" ),
                                         new Parameter( "loginMethod", "umsso" ) );
        Series series = client.getSeries( "http://eo-virtual-archive4.esa.int/search/ER02_SAR_IM__0P/atom?bbox=-75,20,-85,30" );

        series.iterator().next().download( new File( "/tmp" ), new AssertingDownloadHanlder() );
    }

}
