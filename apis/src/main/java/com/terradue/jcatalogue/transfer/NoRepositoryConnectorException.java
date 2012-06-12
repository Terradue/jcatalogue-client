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

/**
 * Thrown in case of an unsupported remote repository type.
 */
public class NoRepositoryConnectorException
    extends CatalogueException
{

    private static final long serialVersionUID = 660356454269328820L;

    private final RemoteRepository repository;

    public NoRepositoryConnectorException( RemoteRepository repository )
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
