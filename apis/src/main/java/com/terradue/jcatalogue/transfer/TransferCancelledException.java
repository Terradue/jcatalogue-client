package com.terradue.jcatalogue.transfer;

import com.terradue.jcatalogue.CatalogueException;
import com.terradue.jcatalogue.repository.RemoteRepository;

/**
 * Thrown in case of an unsupported remote repository type.
 */
public class TransferCancelledException
    extends CatalogueException
{

    private static final long serialVersionUID = 660356454269328820L;

    private final RemoteRepository repository;

    public TransferCancelledException( RemoteRepository repository )
    {
        super( "No connector available to access repository %s (%s) of type %s",
               repository.getId(), repository.getUrl(), repository.getContentType() );
        this.repository = repository;
    }

    public RemoteRepository getRepository()
    {
        return repository;
    }

}
