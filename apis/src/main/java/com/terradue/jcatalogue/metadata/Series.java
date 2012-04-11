package com.terradue.jcatalogue.metadata;

import java.net.URI;
import java.util.Date;

public final class Series
    extends AtomEntity
{

    private final Date beginPosition;

    private final Date endPosition;

    private final GeoLocation geoLocation;

    private Series( String title,
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
                       Iterable<URI> enclosures,
                       Date beginPosition,
                       Date endPosition,
                       GeoLocation geoLocation )
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
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
        this.geoLocation = geoLocation;
    }

    public Date getBeginPosition()
    {
        return beginPosition;
    }

    public Date getEndPosition()
    {
        return endPosition;
    }

    public GeoLocation getGeoLocation()
    {
        return geoLocation;
    }

    public static class Builder
        extends AtomEntityBuilder<Series, Builder>
    {

        private Date beginPosition;

        private Date endPosition;

        private GeoLocation geoLocation;

        public Builder()
        {
            super( Builder.class );
        }

        public Builder setBeginPosition( Date beginPosition )
        {
            this.beginPosition = beginPosition;

            return this;
        }

        public Builder setEndPosition( Date endPosition )
        {
            this.endPosition = endPosition;

            return this;
        }

        public Builder setGeoLocation( GeoLocation geoLocation )
        {
            this.geoLocation = geoLocation;

            return this;
        }

        public Series build()
        {
            return new Series( title,
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
                               enclosures,
                               beginPosition,
                               endPosition,
                               geoLocation );
        }

    }

}
