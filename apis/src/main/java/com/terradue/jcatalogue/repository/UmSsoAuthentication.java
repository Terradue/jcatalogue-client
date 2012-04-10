package com.terradue.jcatalogue.repository;

import static java.util.Arrays.asList;

import static com.terradue.jcatalogue.utils.Objects.checkArgument;

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
