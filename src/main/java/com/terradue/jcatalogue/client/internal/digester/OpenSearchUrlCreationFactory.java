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
