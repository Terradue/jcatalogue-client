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

import org.apache.commons.digester3.binder.AbstractNamespaceURIBasedRulesModule;

public final class LinkedAtomEntityModule
    extends AbstractNamespaceURIBasedRulesModule
{

    public LinkedAtomEntityModule()
    {
        super( ATOM );
    }

    @Override
    protected void configure()
    {
        forPattern( "feed/entry/id" ).callMethod( "addEntityUrl" ).usingElementBodyAsArgument();
    }

}
