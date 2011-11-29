package com.terradue.jcatalogue.client;

import static com.terradue.jcatalogue.client.MimeTypes.ATOM_XML;
import static java.lang.String.format;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = true )
public final class CatalogueDescription
    extends CatalogueEntity
{

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
