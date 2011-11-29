package com.terradue.jcatalogue.client;

/*
 *    Copyright 2011 Terradue srl
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

import java.util.Iterator;

import lombok.Data;

@Data
final class LazyLoadSerieIterator
    implements Iterator<Serie>
{

    private final CatalogueClient catalogueClient;

    private final Iterator<String> serieUrlsIterator;

    @Override
    public boolean hasNext()
    {
        return serieUrlsIterator.hasNext();
    }

    @Override
    public Serie next()
    {
        String nextUrl = serieUrlsIterator.next();
        return catalogueClient.getSerie( nextUrl );
    }

    @Override
    public void remove()
    {
        // not needed, won't be invoked ATM
    }

}
