package com.terradue.jcatalogue.metadata;

import java.net.URI;
import java.util.Date;

public final class Catalogue
    extends AtomEntity
{

    private Catalogue( String title,
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

    public static class Builder
        extends AtomEntityBuilder<Catalogue, Builder>
    {

        public Builder()
        {
            super( Builder.class );
        }

        public Catalogue build()
        {
            return new Catalogue( title,
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
