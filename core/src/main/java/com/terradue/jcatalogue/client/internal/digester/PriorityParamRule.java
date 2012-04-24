package com.terradue.jcatalogue.client.internal.digester;

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

import org.apache.commons.digester3.Rule;
import org.apache.commons.digester3.binder.RuleProvider;
import org.xml.sax.Attributes;

final class PriorityParamRule
    extends Rule
{

    private static final Integer MIN_PRIORITY = 999999;

    private final int paramIndex;

    private PriorityParamRule( int paramIndex )
    {
        this.paramIndex = paramIndex;
    }

    @Override
    public void begin( String namespace, String name, Attributes attributes )
        throws Exception
    {
        Integer priority = MIN_PRIORITY;
        String priorityValue = attributes.getValue( "urn:ietf:params:xml:ns:metalink", "priority" );

        if ( priorityValue != null )
        {
            priority = Integer.valueOf( priorityValue );
        }

        getDigester().peekParams()[paramIndex] = priority;
    }

    public static final class PriorityParamRuleProvider
        implements RuleProvider<PriorityParamRule>
    {

        private final int paramIndex;

        public PriorityParamRuleProvider( int paramIndex )
        {
            this.paramIndex = paramIndex;
        }

        @Override
        public PriorityParamRule get()
        {
            return new PriorityParamRule( paramIndex );
        }

    }

}
