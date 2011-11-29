package com.terradue.jcatalogue.client.digester;

import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;

import com.terradue.jcatalogue.client.OpenSearchUrl;

final class OpenSearchUrlCreationFactory
    extends AbstractObjectCreationFactory<OpenSearchUrl>
{

    @Override
    public OpenSearchUrl createObject( Attributes attributes )
        throws Exception
    {
        String type = attributes.getValue( "type" );
        String template = attributes.getValue( "template" );

        return new OpenSearchUrl( type, template );
    }

}
