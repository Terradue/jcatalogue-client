package com.terradue.jcatalogue.client.digester;

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
