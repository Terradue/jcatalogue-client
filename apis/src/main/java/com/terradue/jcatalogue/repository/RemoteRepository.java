package com.terradue.jcatalogue.repository;

import static java.lang.String.format;

import static com.terradue.jcatalogue.repository.ChecksumPolicy.IGNORE;
import static com.terradue.jcatalogue.repository.UpdatePolicy.ALWAYS;

import static com.terradue.jcatalogue.utils.Objects.checkArgument;
import static com.terradue.jcatalogue.utils.Objects.eq;
import static com.terradue.jcatalogue.utils.Objects.hash;

import java.net.URL;

/**
 * A repository on a remote server.
 */
public final class RemoteRepository
    implements ProductRepository
{

    private final String id;

    private final String contentType;

    private URL url;

    private Proxy proxy;

    private Authentication authentication;

    private UpdatePolicy updatePolicy = ALWAYS;

    private ChecksumPolicy checksumPolicy = IGNORE;

    /**
     * Creates a new repository with the specified properties and the default policies.
     *
     * @param id The identifier of the repository, may be {@code null}.
     * @param type The type of the repository, may be {@code null}.
     * @param url The (base) URL of the repository, may be {@code null}.
     */
    public RemoteRepository( String id, String type, URL url )
    {
        checkArgument( id != null, "Argument 'id' must not be null" );
        checkArgument( type != null, "Argument 'type' must not be null" );
        checkArgument( url != null, "Argument 'url' must not be null" );

        this.id = id;
        this.contentType = type;
        this.url = url;
    }

    public String getId()
    {
        return id;
    }

    public String getContentType()
    {
        return contentType;
    }

    /**
     * Gets the (base) URL of this repository.
     *
     * @return The (base) URL of this repository, never {@code null}.
     */
    public URL getUrl()
    {
        return url;
    }

    /**
     * Gets the proxy that has been selected for this repository.
     *
     * @return The selected proxy or {@code null} if none.
     */
    public Proxy getProxy()
    {
        return proxy;
    }

    /**
     * Sets the proxy to use in order to access this repository.
     *
     * @param proxy The proxy to use, may be {@code null}.
     * @return This repository for chaining, never {@code null}.
     */
    public RemoteRepository setProxy( Proxy proxy )
    {
        this.proxy = proxy;

        return this;
    }

    /**
     * Gets the authentication that has been selected for this repository.
     *
     * @return The selected authentication or {@code null} if none.
     */
    public Authentication getAuthentication()
    {
        return authentication;
    }

    /**
     * Sets the authentication to use in order to access this repository.
     *
     * @param authentication The authentication to use, may be {@code null}.
     * @return This repository for chaining, never {@code null}.
     */
    public RemoteRepository setAuthentication( Authentication authentication )
    {
        this.authentication = authentication;

        return this;
    }

    public ChecksumPolicy getChecksumPolicy()
    {
        return checksumPolicy;
    }

    public RemoteRepository setChecksumPolicy( ChecksumPolicy checksumPolicy )
    {
        this.checksumPolicy = checksumPolicy;

        return this;
    }

    public UpdatePolicy getUpdatePolicy()
    {
        return updatePolicy;
    }

    public RemoteRepository setUpdatePolicy( UpdatePolicy updatePolicy )
    {
        this.updatePolicy = updatePolicy;

        return this;
    }

    @Override
    public String toString()
    {
        return format( "%s (%s)", id, url );
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

        RemoteRepository that = (RemoteRepository) obj;

        return eq( url, that.url )
                        && eq( contentType, that.contentType )
                        && eq( id, that.id )
                        && eq( proxy, that.proxy )
                        && eq( authentication, that.authentication )
                        && eq( checksumPolicy, that.checksumPolicy )
                        && eq( updatePolicy, that.updatePolicy );
    }

    @Override
    public int hashCode()
    {
        int hash = 17;
        hash = hash * 31 + hash( url );
        hash = hash * 31 + hash( contentType );
        hash = hash * 31 + hash( id );
        hash = hash * 31 + hash( proxy );
        hash = hash * 31 + hash( authentication );
        hash = hash * 31 + hash( checksumPolicy );
        hash = hash * 31 + hash( updatePolicy );
        return hash;
    }

}
