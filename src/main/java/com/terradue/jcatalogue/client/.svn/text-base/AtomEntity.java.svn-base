package com.terradue.jcatalogue.client;

import static com.terradue.jcatalogue.client.MimeTypes.ATOM_XML;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode( callSuper = false )
abstract class AtomEntity
    extends CatalogueEntity
{

    private static final String NEXT = "next";

    private static final String ENCLOSURE = "enclosure";

    @Getter( AccessLevel.PACKAGE )
    private final List<String> enitiesUrls = new ArrayList<String>();

    private String title;

    private String subtitle;

    private String content;

    private Date published;

    private Date updated;

    private String id;

    private int totalResults;

    private int startIndex;

    private int itemsPerPage;

    @Getter( AccessLevel.PACKAGE )
    private String nextResultsUri;

    @Getter( AccessLevel.PACKAGE )
    private String enclosure;

    public void addLink( String rel, String type, String href )
    {
        if ( NEXT.equals( rel ) && ATOM_XML.equals( type ) )
        {
            nextResultsUri = href;
        }
        else if ( ENCLOSURE.equals( rel ) )
        {
            enclosure = href;
        }
    }

    public final boolean hasMoreResults()
    {
        return nextResultsUri != null;
    }

    public final void addEntityUrl( String serieUrl )
    {
        enitiesUrls.add( serieUrl );
    }

}
