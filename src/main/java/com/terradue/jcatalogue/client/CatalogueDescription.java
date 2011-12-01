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
import static java.lang.String.format;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode( callSuper = true )
public final class CatalogueDescription
    extends CatalogueEntity
{

    @Getter( AccessLevel.NONE )
    private final Map<String, OpenSearchUrl> typeUrlTemplates = new HashMap<String, OpenSearchUrl>();

    private String shortName;

    private String longName;

    private String description;

    private String[] tags;

    private String contact;

    private String developer;

    private String attribution;

    private String syndicationRight;

    private boolean adultContent;

    private Locale language;

    private Charset inputEncoding;

    private Charset outputEncoding;

    public void addOpenSearchUrl( OpenSearchUrl url )
    {
        typeUrlTemplates.put( url.getType(), url );
    }

    @Override
    void setCatalogueClient( CatalogueClient catalogueClient )
    {
        super.setCatalogueClient( catalogueClient );
        for ( OpenSearchUrl url : typeUrlTemplates.values() )
        {
            url.setCatalogueClient( catalogueClient );
        }
    }

    public Catalogue getCatalogue( Parameter...parameters )
    {
        if ( !typeUrlTemplates.containsKey( ATOM_XML ) )
        {
            throw new IllegalStateException( format( "", ATOM_XML ) );
        }

        return typeUrlTemplates.get( ATOM_XML ).invoke( parameters );
    }

}
