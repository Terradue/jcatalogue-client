package com.terradue.jcatalogue.client.digester;

import org.apache.commons.digester3.binder.AbstractRulesModule;

public final class LinkedAtomEntityModule
    extends AbstractRulesModule
{

    @Override
    protected void configure()
    {
        forPattern( "feed/entry/id" ).callMethod( "addEntityUrl" ).usingElementBodyAsArgument();
    }

}
