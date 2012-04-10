package com.terradue.jcatalogue.transfer;

import java.net.URI;

import com.terradue.jcatalogue.CatalogueException;
import com.terradue.jcatalogue.repository.RemoteRepository;

public final class MetadataNotFoundException
    extends CatalogueException
{

    /**
     *
     */
    private static final long serialVersionUID = 1599718544968499058L;

    private final URI metadataLocation;

    private final RemoteRepository remoteRepository;

    public MetadataNotFoundException( URI metadataLocation, RemoteRepository remoteRepository )
    {
        super( "Could not find metadata %s in %s", metadataLocation, remoteRepository.getId() );
        this.metadataLocation = metadataLocation;
        this.remoteRepository = remoteRepository;
    }

    public URI getMetadataLocation()
    {
        return metadataLocation;
    }

    public RemoteRepository getRemoteRepository()
    {
        return remoteRepository;
    }

}
