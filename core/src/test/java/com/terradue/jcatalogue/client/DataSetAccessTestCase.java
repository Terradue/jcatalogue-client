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

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class DataSetAccessTestCase
{

    private CatalogueClient client;

    @Before
    public void setUp()
    {
        client = new CatalogueClient();
    }

    @After
    public void tearDown()
    {
        client.shutDown();
        client = null;
    }

    /**
     * @since 0.2
     */
    @Test
    public void accessToDataSet()
        throws Exception
    {
        CatalogueClient client = new CatalogueClient();
        DataSet dataSet = client.getDataSet( "http://10.11.12.248/catalogue/gpod/ASA_IM__0P/ASA_IM__0CNPDE20110409_004512_000000163101_00189_47617_2648.N1/atom" );

        assertNotNull( dataSet );
    }

}
