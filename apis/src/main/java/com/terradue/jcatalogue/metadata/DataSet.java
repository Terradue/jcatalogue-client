package com.terradue.jcatalogue.metadata;

import java.net.URI;
import java.util.Date;

public final class DataSet
    extends AtomEntity
{

    private DataSet( String title,
                       String subtitle,
                       String content,
                       Date published,
                       Date updated,
                       String id,
                       int totalResults,
                       int startIndex,
                       int itemsPerPage,
                       String nextResultsUri,
                       Iterable<String> entitiesUrl,
                       Iterable<URI> enclosures )
    {
        super( title,
               subtitle,
               content,
               published,
               updated,
               id,
               totalResults,
               startIndex,
               itemsPerPage,
               nextResultsUri,
               entitiesUrl,
               enclosures );
    }

    public static abstract class Builder
        extends AtomEntityBuilder<Builder>
    {

        public Builder()
        {
            super( Builder.class );
        }

        public DataSet build()
        {
            return new DataSet( title,
                                subtitle,
                                content,
                                published,
                                updated,
                                id,
                                totalResults,
                                startIndex,
                                itemsPerPage,
                                nextResultsUri,
                                entitiesUrl,
                                enclosures );
        }

    }

}
