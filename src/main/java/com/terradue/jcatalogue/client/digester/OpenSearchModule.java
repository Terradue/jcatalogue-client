package com.terradue.jcatalogue.client.digester;

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

import org.apache.commons.digester3.binder.AbstractRulesModule;

import com.terradue.jcatalogue.client.CatalogueDescription;

public final class OpenSearchModule
    extends AbstractRulesModule
{

    @Override
    protected void configure()
    {
        forPattern( "OpenSearchDescription" ).createObject().ofType( CatalogueDescription.class );
        forPattern( "OpenSearchDescription/ShortName" ).setBeanProperty().withName( "shortName" );
        forPattern( "OpenSearchDescription/LongName" ).setBeanProperty().withName( "longName" );
        forPattern( "OpenSearchDescription/Description" ).setBeanProperty().withName( "description" );
        forPattern( "OpenSearchDescription/Tags" ).setBeanProperty().withName( "tags" );
        forPattern( "OpenSearchDescription/ShortName" ).setBeanProperty().withName( "shortName" );
        forPattern( "OpenSearchDescription/Contact" ).setBeanProperty().withName( "contact" );
        forPattern( "OpenSearchDescription/Developer" ).setBeanProperty().withName( "developer" );
        forPattern( "OpenSearchDescription/Attribution" ).setBeanProperty().withName( "attribution" );
        forPattern( "OpenSearchDescription/SyndicationRight" ).setBeanProperty().withName( "syndicationRight" );
        forPattern( "OpenSearchDescription/AdultContent" ).setBeanProperty().withName( "adultContent" );
        forPattern( "OpenSearchDescription/Language" ).setBeanProperty().withName( "language" );
        forPattern( "OpenSearchDescription/InputEncoding" ).setBeanProperty().withName( "inputEncoding" );
        forPattern( "OpenSearchDescription/OutputEncoding" ).setBeanProperty().withName( "outputEncoding" );

        forPattern( "OpenSearchDescription/Url" )
            .factoryCreate()
            .usingFactory( new OpenSearchUrlCreationFactory() )
            .then()
            .setNext( "addOpenSearchUrl" );
    }

}
