package com.terradue.jcatalogue.client.umsso;

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

import static java.lang.String.format;
import static com.terradue.jcatalogue.client.utils.Assertions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.ning.http.client.Cookie;
import com.terradue.jcatalogue.client.Parameter;

/**
 * @since 0.8
 */
@Getter
@ToString
public final class Credentials
{

    private static final XPath XPATH_COMPILER = XPathFactory.newInstance().newXPath();

    private final XPathExpression loginFormActionXPath;

    private final List<Parameter> parameters;

    @Setter
    private Cookie ssoCookie;

    private Credentials( XPathExpression loginFormActionXPath, List<Parameter> parameters )
    {
        this.loginFormActionXPath = loginFormActionXPath;
        this.parameters = parameters;
    }

    public static final class Builder
    {

        private final List<Parameter> parameters = new LinkedList<Parameter>();

        private final String loginFormActionXPath;

        public Builder( String loginFormActionXPath )
        {
            this.loginFormActionXPath = checkNotNull( loginFormActionXPath, "loginFormActionXPath must be not null" );
        }

        public Builder addParameter( String name, String value )
        {
            name = checkNotNull( name, "Parameter name must be not null" );
            value = checkNotNull( value, "Parameter value must be not null" );

            parameters.add( new Parameter( name, value ) );
            return this;
        }

        public Credentials build()
            throws Exception
        {
            return new Credentials( XPATH_COMPILER.compile( format( "%s/@action", loginFormActionXPath.toUpperCase() ) ),
                                    parameters );
        }

    }

}
