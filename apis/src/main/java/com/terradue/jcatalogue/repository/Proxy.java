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

import static com.terradue.jcatalogue.lang.Preconditions.eq;
import static com.terradue.jcatalogue.lang.Preconditions.hash;

/**
 * A proxy to use for connections to a repository. <em>Note:</em> Instances of this class are immutable and the exposed
 * mutators return new objects rather than changing the current instance.
 */
public final class Proxy
{

    /**
     * Type denoting a proxy for HTTP transfers.
     */
    public static final String TYPE_HTTP = "http";

    /**
     * Type denoting a proxy for HTTPS transfers.
     */
    public static final String TYPE_HTTPS = "https";

    private final String type;

    private final String host;

    private final int port;

    private final Authentication auth;

    /**
     * Creates a new proxy with the specified properties.
     *
     * @param type The type of the proxy, e.g. "http", may be {@code null}.
     * @param host The host of the proxy, may be {@code null}.
     * @param port The port of the proxy.
     * @param auth The authentication to use for the proxy connection, may be {@code null}.
     */
    public Proxy( String type, String host, int port, Authentication auth )
    {
        this.type = ( type != null ) ? type : "";
        this.host = ( host != null ) ? host : "";
        this.port = port;
        this.auth = auth;
    }

    /**
     * Gets the type of this proxy.
     *
     * @return The type of this proxy, never {@code null}.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of the proxy.
     *
     * @param type The type of the proxy, e.g. "http", may be {@code null}.
     * @return The new proxy, never {@code null}.
     */
    public Proxy setType( String type )
    {
        if ( this.type.equals( type ) || ( type == null && this.type.length() <= 0 ) )
        {
            return this;
        }
        return new Proxy( type, host, port, auth );
    }

    /**
     * Gets the host for this proxy.
     *
     * @return The host for this proxy, never {@code null}.
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Sets the host of the proxy.
     *
     * @param host The host of the proxy, may be {@code null}.
     * @return The new proxy, never {@code null}.
     */
    public Proxy setHost( String host )
    {
        if ( this.host.equals( host ) || ( host == null && this.host.length() <= 0 ) )
        {
            return this;
        }
        return new Proxy( type, host, port, auth );
    }

    /**
     * Gets the port number for this proxy.
     *
     * @return The port number for this proxy.
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Sets the port number for the proxy.
     *
     * @param port The port number for the proxy.
     * @return The new proxy, never {@code null}.
     */
    public Proxy setPort( int port )
    {
        if ( this.port == port )
        {
            return this;
        }
        return new Proxy( type, host, port, auth );
    }

    /**
     * Gets the authentication to use for the proxy connection.
     *
     * @return The authentication to use or {@code null} if none.
     */
    public Authentication getAuthentication()
    {
        return auth;
    }

    /**
     * Sets the authentication to use for the proxy connection.
     *
     * @param auth The authentication to use, may be {@code null}.
     * @return The new proxy, never {@code null}.
     */
    public Proxy setAuthentication( Authentication auth )
    {
        if ( eq( this.auth, auth ) )
        {
            return this;
        }
        return new Proxy( type, host, port, auth );
    }

    @Override
    public String toString()
    {
        return getHost() + ':' + getPort();
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null || !getClass().equals( obj.getClass() ) )
        {
            return false;
        }

        Proxy that = (Proxy) obj;

        return eq( type, that.type ) && eq( host, that.host ) && port == that.port && eq( auth, that.auth );
    }

    @Override
    public int hashCode()
    {
        return hash( 17, 31, host, type, port, auth );
    }

}
