package com.terradue.jcatalogue.transfer;

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
