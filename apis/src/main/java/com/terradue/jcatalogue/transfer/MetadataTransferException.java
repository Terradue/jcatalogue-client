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
