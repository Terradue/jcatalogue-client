package com.terradue.jcatalogue.client;

import java.util.Iterator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = true )
public final class Catalogue
    extends AtomEntity
    implements Iterable<Serie>
{

    @Override
    public Iterator<Serie> iterator()
    {
        return new LazyLoadSerieIterator( getCatalogueClient(), getEnitiesUrls().iterator() );
    }

    public Catalogue getNextResults()
    {
        if ( !hasMoreResults() )
        {
            throw new IllegalStateException( "More results not available" );
        }

        return getCatalogueClient().getCatalogue( getNextResultsUri() );
    }

}
