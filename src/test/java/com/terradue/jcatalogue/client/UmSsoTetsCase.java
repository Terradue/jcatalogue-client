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

import org.junit.Test;

public final class UmSsoTetsCase
{

    /**
     * @since 0.8
     */
    @Test
    public void ssoLogin()
        throws Exception
    {
        CatalogueClient client = new CatalogueClient( new CatalogueClient.Configuration().
                                                      registerUmSsoCredentials( URI.create( "https://eo-sso-idp.eo.esa.int/idp/umsso20/login?null" ),
                                                                                 HttpMethod.POST,
                                                                                 new Parameter( "cn", "stripodi" ),
                                                                                 new Parameter( "password", "Princesa_1979" ),
                                                                                 new Parameter( "idleTime", "oneday" ),
                                                                                 new Parameter( "sessionTime", "untilbrowserclose" ),
                                                                                 new Parameter( "loginFields", "cn@password" ),
                                                                                 new Parameter( "loginMethod", "umsso" ) ) );
        Series series = client.getSeries( "http://eo-virtual-archive4.esa.int/search/ER02_SAR_IM__0P/atom?bbox=-75,20,-85,30" );

        series.iterator().next().download( new File( "/tmp" ), new AssertingDownloadHanlder() );
    }

}
