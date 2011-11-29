package com.terradue.jcatalogue.client;

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
