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

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.digester3.binder.AbstractRulesModule;

@Data
@EqualsAndHashCode( callSuper = false )
public final class AtomRulesModule
    extends AbstractRulesModule
{

    private final Class<?> mappedEntity;

    @Override
    protected void configure()
    {
        forPattern( "feed" ).createObject().ofType( mappedEntity );
        forPattern( "feed/title" ).setBeanProperty();
        forPattern( "feed/subtitle" ).setBeanProperty();
        forPattern( "feed/updated" ).setBeanProperty();
        forPattern( "feed/id" ).setBeanProperty();
        forPattern( "feed/totalResults" ).setBeanProperty();
        forPattern( "feed/startIndex" ).setBeanProperty();
        forPattern( "feed/itemsPerPage" ).setBeanProperty();

        forPattern( "feed/link" ).callMethod( "addLink" )
                                     .withParamTypes( String.class, String.class, String.class )
                                 .then()
                                 .callParam()
                                     .fromAttribute( "rel" )
                                     .ofIndex( 0 )
                                     .then()
                                 .callParam()
                                     .fromAttribute( "type" )
                                     .ofIndex( 1 )
                                     .then()
                                 .callParam()
                                     .fromAttribute( "href" )
                                     .ofIndex( 2 );
    }

}
