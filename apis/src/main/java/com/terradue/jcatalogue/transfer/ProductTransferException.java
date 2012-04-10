package com.terradue.jcatalogue.transfer;

import static java.lang.String.format;

import com.terradue.jcatalogue.CatalogueException;
import com.terradue.jcatalogue.repository.RemoteRepository;

public final class ProductTransferException
    extends CatalogueException
{

    /**
     *
     */
    private static final long serialVersionUID = 128196030104825613L;

    private final String productUrl;

    private final RemoteRepository remoteRepository;

    public ProductTransferException( String productUrl, RemoteRepository remoteRepository, Throwable cause )
    {
        super( format( "Could not transfer product %s from %s: %s", productUrl, remoteRepository.getId() ), cause );
        this.productUrl = productUrl;
        this.remoteRepository = remoteRepository;
    }

    public String getProductUrl()
    {
        return productUrl;
    }

    public RemoteRepository getRemoteRepository()
    {
        return remoteRepository;
    }

}
