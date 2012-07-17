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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public final class QueryParametersTestCase
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
     * @since 0.3
     */
    @Test
    @Ignore // someone dropped the entry...
    public void testQueryParameters()
        throws Exception
    {
        Series serie = client.getSeries( "http://10.11.12.248/catalogue/gpod/ER2_TIM_AX/atom",
                                         new Parameter( "startDate", "1995-07-18T14:46:54.000" ),
                                         new Parameter( "stopDate", "1995-07-18T14:46:54.000" ) );

        assertTrue( serie.getTotalResults() > 0 );
    }

}
