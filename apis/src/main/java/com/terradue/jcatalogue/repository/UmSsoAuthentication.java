package com.terradue.jcatalogue.repository;

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

import static java.util.Arrays.asList;

import static com.terradue.jcatalogue.lang.Preconditions.checkArgument;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import com.terradue.jcatalogue.HttpMethod;

public final class UmSsoAuthentication
    implements Authentication
{

    private final URI loginFormUrl;

    private final HttpMethod httpMethod;

    private final List<AuthParameter> authParameters;

    public UmSsoAuthentication( URI loginFormUrl, HttpMethod httpMethod, AuthParameter...authParameters )
    {
        checkArgument( loginFormUrl != null, "Argument 'loginFormUrl' must be not null" );
        checkArgument( httpMethod != null, "Argument 'httpMethod' must be not null" );
        checkArgument( authParameters != null, "Argument 'authParameters' must be not null" );

        this.loginFormUrl = loginFormUrl;
        this.httpMethod = httpMethod;
        this.authParameters = asList( authParameters );
    }

    public URI getLoginFormUrl()
    {
        return loginFormUrl;
    }

    public HttpMethod getHttpMethod()
    {
        return httpMethod;
    }

    public Iterator<AuthParameter> getAuthParameters()
    {
        return authParameters.iterator();
    }

    public static final class AuthParameter
    {

        public static AuthParameter authParameter( String name, String value )
        {
            return new AuthParameter( name, value );
        }

        private final String name;

        private final String value;

        public AuthParameter( String name, String value )
        {
            checkArgument( name != null, "Argument 'name' must be not null" );
            checkArgument( value != null, "Argument 'value' must be not null" );

            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public String getValue()
        {
            return value;
        }

    }

}
