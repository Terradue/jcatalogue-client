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

import static com.terradue.jcatalogue.client.MimeTypes.ATOM_XML;

import java.net.URI;
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
    private final List<URI> enclosures = new ArrayList<URI>();

    public void addLink( String rel, String type, String href )
    {
        if ( NEXT.equals( rel ) && ATOM_XML.equals( type ) )
        {
            nextResultsUri = href;
        }
        else if ( ENCLOSURE.equals( rel ) )
        {
            enclosures.add( URI.create( href ) );
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
