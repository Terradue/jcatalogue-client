package com.terradue.jcatalogue.transfer;

import static java.lang.String.format;

import java.net.URI;

import com.terradue.jcatalogue.CatalogueException;
import com.terradue.jcatalogue.repository.RemoteRepository;

public final class MetadataTransferException
    extends CatalogueException
{

    /**
     *
     */
    private static final long serialVersionUID = 128196030104825613L;

    private final URI metadataLocation;

    private final RemoteRepository remoteRepository;

    public MetadataTransferException( URI metadataLocation, RemoteRepository remoteRepository, Throwable cause )
    {
        super( format( "Could not transfer metadata %s from %s: %s",
                       metadataLocation, remoteRepository.getId() ), cause );
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
