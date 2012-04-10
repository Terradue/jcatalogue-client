package com.terradue.jcatalogue.transfer;

import com.terradue.jcatalogue.CatalogueException;
import com.terradue.jcatalogue.repository.RemoteRepository;

public final class ProductNotFoundException
    extends CatalogueException
{

    /**
     *
     */
    private static final long serialVersionUID = 1599718544968499058L;

    private final String productUrl;

    private final RemoteRepository remoteRepository;

    public ProductNotFoundException( String productUrl, RemoteRepository remoteRepository )
    {
        super( "Could not find metadata %s in %s", productUrl, remoteRepository.getId() );
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
