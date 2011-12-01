package com.terradue.jcatalogue.client.internal.digester;

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

import static com.terradue.jcatalogue.client.internal.digester.Namespaces.ATOM;
import static com.terradue.jcatalogue.client.internal.digester.Namespaces.GML;

import org.apache.commons.digester3.binder.AbstractNamespaceURIBasedRulesModule;

import com.terradue.jcatalogue.client.DataSet;
import com.terradue.jcatalogue.client.geo.Box;
import com.terradue.jcatalogue.client.geo.Line;
import com.terradue.jcatalogue.client.geo.Point;
import com.terradue.jcatalogue.client.geo.Polygon;

public final class DataSetRulesModule
    extends AbstractNamespaceURIBasedRulesModule
{

    public DataSetRulesModule()
    {
        super( ATOM );
    }

    @Override
    protected void configure()
    {
        forPattern( "feed/entry" ).createObject().ofType( DataSet.class )
                                  .then()
                                  .setNext( "addDataSet" );
        forPattern( "feed/entry/title" ).setBeanProperty();
        forPattern( "feed/entry/subtitle" ).setBeanProperty();
        forPattern( "feed/entry/updated" ).setBeanProperty();
        forPattern( "feed/entry/id" ).setBeanProperty();

        forPattern( "feed/entry/validTime/TimePeriod/beginPosition" )
            .withNamespaceURI( GML )
            .setBeanProperty().withName( "beginPosition" );
        forPattern( "feed/entry/validTime/TimePeriod/beginPosition" )
            .withNamespaceURI( GML )
            .setBeanProperty().withName( "endPosition" );

        forPattern( "feed/entry/where/Envelope" )
            .withNamespaceURI( GML )
            .createObject().ofType( Box.class );
        forPattern( "feed/entry/where/Envelope/lowerCorner" )
            .withNamespaceURI( GML )
            .setBeanProperty().withName( "lowerCorner" );
        forPattern( "feed/entry/where/Envelope/upperCorner" )
            .withNamespaceURI( GML )
            .setBeanProperty().withName( "upperCorner" );
        forPattern( "feed/entry/where/LineString/posList" )
            .withNamespaceURI( GML )
            .addRuleCreatedBy( new SetGeoDataLocationRule.Factory( Line.class ) );
        forPattern( "feed/entry/where/Polygon/exterior/LinearRing/posList" )
            .withNamespaceURI( GML )
            .addRuleCreatedBy( new SetGeoDataLocationRule.Factory( Polygon.class ) );
        forPattern( "feed/entry/where/Point/pos" )
            .withNamespaceURI( GML )
            .addRuleCreatedBy( new SetGeoDataLocationRule.Factory( Point.class ) );

        forPattern( "feed/entry/link" ).callMethod( "addLink" )
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
