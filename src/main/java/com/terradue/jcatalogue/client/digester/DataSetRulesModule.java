package com.terradue.jcatalogue.client.digester;

import org.apache.commons.digester3.binder.AbstractRulesModule;

import com.terradue.jcatalogue.client.DataSet;

public final class DataSetRulesModule
    extends AbstractRulesModule
{

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
