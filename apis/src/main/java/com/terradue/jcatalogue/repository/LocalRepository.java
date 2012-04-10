package com.terradue.jcatalogue.repository;

import static com.terradue.jcatalogue.utils.Objects.checkArgument;
import static com.terradue.jcatalogue.utils.Objects.eq;
import static com.terradue.jcatalogue.utils.Objects.hash;

import java.io.File;

/**
 * A repository on the local file system used to cache contents of remote repositories and to store locally installed
 * artifacts. Note that this class merely describes such a repository, actual access to the contained artifacts is
 * handled by a {@link LocalRepositoryManager} which is usually determined from the {@link #getContentType() type} of
 * the repository.
 */
public final class LocalRepository
    implements ProductRepository
{

    private static final String LOCAL = "local";

    private final File basedir;

    private final String type;

    /**
     * Creates a new local repository with the specified base directory and unknown type.
     *
     * @param basedir The base directory of the repository, may be {@code null}.
     */
    public LocalRepository( File basedir )
    {
        this( basedir, "" );
    }

    /**
     * Creates a new local repository with the specified properties.
     *
     * @param basedir The base directory of the repository, may be {@code null}.
     * @param type The type of the repository, may be {@code null}.
     */
    public LocalRepository( File basedir, String type )
    {
        checkArgument( basedir != null, "Argument 'basedir' must be not null" );
        checkArgument( !basedir.isFile(), "Basedir %s points to a file, while a directory is required" );
        this.basedir = basedir;
        this.type = ( type != null ) ? type : "";
    }

    public String getContentType()
    {
        return type;
    }

    public String getId()
    {
        return LOCAL;
    }

    /**
     * Gets the base directory of the repository.
     *
     * @return The base directory or {@code null} if none.
     */
    public File getBasedir()
    {
        return basedir;
    }

    @Override
    public String toString()
    {
        return getBasedir() + " (" + getContentType() + ")";
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

        LocalRepository that = (LocalRepository) obj;

        return eq( basedir, that.basedir ) && eq( type, that.type );
    }

    @Override
    public int hashCode()
    {
        int hash = 17;
        hash = hash * 31 + hash( basedir );
        hash = hash * 31 + hash( type );
        return hash;
    }

}
