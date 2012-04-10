package com.terradue.jcatalogue.metadata;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

abstract class AtomEntity
{

    private final String title;

    private final String subtitle;

    private final String content;

    private final Date published;

    private final Date updated;

    private final String id;

    private final int totalResults;

    private final int startIndex;

    private final int itemsPerPage;

    private final String nextResultsUri;

    private final Iterable<String> entitiesUrl;

    private final Iterable<URI> enclosures;

    public AtomEntity( String title,
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
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.published = published;
        this.updated = updated;
        this.id = id;
        this.totalResults = totalResults;
        this.startIndex = startIndex;
        this.itemsPerPage = itemsPerPage;
        this.nextResultsUri = nextResultsUri;
        this.entitiesUrl = entitiesUrl;
        this.enclosures = enclosures;
    }

    public final String getTitle()
    {
        return title;
    }

    public final String getSubtitle()
    {
        return subtitle;
    }

    public final String getContent()
    {
        return content;
    }

    public final Date getPublished()
    {
        return published;
    }

    public final Date getUpdated()
    {
        return updated;
    }

    public final String getId()
    {
        return id;
    }

    public final int getTotalResults()
    {
        return totalResults;
    }

    public final int getStartIndex()
    {
        return startIndex;
    }

    public final int getItemsPerPage()
    {
        return itemsPerPage;
    }

    public final String getNextResultsUri()
    {
        return nextResultsUri;
    }

    public final Iterable<String> getEntitiesUrl()
    {
        return entitiesUrl;
    }

    public final Iterable<URI> getEnclosures()
    {
        return enclosures;
    }

    public final boolean hasMoreResults()
    {
        return nextResultsUri != null;
    }

    protected static abstract class AtomEntityBuilder<T extends AtomEntityBuilder<T>>
    {

        private static final String NEXT = "next";

        private static final String ENCLOSURE = "enclosure";

        private static final String ATOM_XML = "application/atom+xml";

        protected String title;

        protected String subtitle;

        protected String content;

        protected Date published;

        protected Date updated;

        protected String id;

        protected int totalResults;

        protected int startIndex;

        protected int itemsPerPage;

        protected String nextResultsUri;

        protected final Collection<String> entitiesUrl = new LinkedList<String>();

        protected final Collection<URI> enclosures = new LinkedList<URI>();

        private final Class<T> derived;

        public AtomEntityBuilder( Class<T> derived )
        {
            this.derived = derived;
        }

        public T setTitle( String title )
        {
            this.title = title;

            return derived.cast( this );
        }

        public T setSubtitle( String subtitle )
        {
            this.subtitle = subtitle;

            return derived.cast( this );
        }

        public T setContent( String content )
        {
            this.content = content;

            return derived.cast( this );
        }

        public T setPublished( Date published )
        {
            this.published = published;

            return derived.cast( this );
        }

        public T setUpdated( Date updated )
        {
            this.updated = updated;

            return derived.cast( this );
        }

        public T setId( String id )
        {
            this.id = id;

            return derived.cast( this );
        }

        public T setTotalResults( int totalResults )
        {
            this.totalResults = totalResults;

            return derived.cast( this );
        }

        public T setStartIndex( int startIndex )
        {
            this.startIndex = startIndex;

            return derived.cast( this );
        }

        public T setItemsPerPage( int itemsPerPage )
        {
            this.itemsPerPage = itemsPerPage;

            return derived.cast( this );
        }

        public T setNextResultsUri( String nextResultsUri )
        {
            this.nextResultsUri = nextResultsUri;

            return derived.cast( this );
        }

        public T addLink( String rel, String type, String href )
        {
            if ( NEXT.equals( rel ) && ATOM_XML.equals( type ) )
            {
                nextResultsUri = href;
            }
            else if ( ENCLOSURE.equals( rel ) )
            {
                enclosures.add( URI.create( href ) );
            }

            return derived.cast( this );
        }

    }

}
