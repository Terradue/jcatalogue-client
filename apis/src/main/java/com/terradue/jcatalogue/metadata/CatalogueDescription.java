package com.terradue.jcatalogue.metadata;

import static com.terradue.jcatalogue.metadata.UrlTemplate.parseUrlTemplate;
import static com.terradue.jcatalogue.utils.Objects.checkArgument;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class CatalogueDescription
{

    private final String shortName;

    private final String longName;

    private final String description;

    private final String[] tags;

    private final String contact;

    private final String developer;

    private final String attribution;

    private final String syndicationRight;

    private final boolean adultContent;

    private final Locale language;

    private final Charset inputEncoding;

    private final Charset outputEncoding;

    private final Map<String, UrlTemplate> typeUrlTemplates;

    public CatalogueDescription( String shortName,
                                 String longName,
                                 String description,
                                 String[] tags,
                                 String contact,
                                 String developer,
                                 String attribution,
                                 String syndicationRight,
                                 boolean adultContent,
                                 Locale language,
                                 Charset inputEncoding,
                                 Charset outputEncoding,
                                 Map<String, UrlTemplate> typeUrlTemplates )
    {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.tags = tags;
        this.contact = contact;
        this.developer = developer;
        this.attribution = attribution;
        this.syndicationRight = syndicationRight;
        this.adultContent = adultContent;
        this.language = language;
        this.inputEncoding = inputEncoding;
        this.outputEncoding = outputEncoding;
        this.typeUrlTemplates = typeUrlTemplates;
    }

    public String getShortName()
    {
        return shortName;
    }

    public String getLongName()
    {
        return longName;
    }

    public String getDescription()
    {
        return description;
    }

    public String[] getTags()
    {
        return tags;
    }

    public String getContact()
    {
        return contact;
    }

    public String getDeveloper()
    {
        return developer;
    }

    public String getAttribution()
    {
        return attribution;
    }

    public String getSyndicationRight()
    {
        return syndicationRight;
    }

    public boolean isAdultContent()
    {
        return adultContent;
    }

    public Locale getLanguage()
    {
        return language;
    }

    public Charset getInputEncoding()
    {
        return inputEncoding;
    }

    public Charset getOutputEncoding()
    {
        return outputEncoding;
    }

    public Iterable<UrlTemplate> getUrlTemplates()
    {
        return typeUrlTemplates.values();
    }

    public UrlTemplate getUrlTemplate( String type )
    {
        return typeUrlTemplates.get( type );
    }

    public static final class Builder
    {

        private final Map<String, UrlTemplate> typeUrlTemplates = new HashMap<String, UrlTemplate>();

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

        public Builder()
        {
            // do nothing
        }

        public Builder( CatalogueDescription prototype )
        {
            checkArgument( prototype != null, "Argument 'prototype' must not be null" );

            setShortName( prototype.shortName )
            .setLongName( prototype.longName )
            .setDescription( prototype.description )
            .setTags( prototype.tags )
            .setContact( prototype.contact )
            .setDeveloper( prototype.developer )
            .setAttribution( prototype.attribution )
            .setSyndicationRight( prototype.syndicationRight )
            .setAdultContent( prototype.adultContent )
            .setLanguage( prototype.language )
            .setInputEncoding( prototype.inputEncoding )
            .setOutputEncoding( prototype.outputEncoding );

            typeUrlTemplates.putAll( prototype.typeUrlTemplates );
        }

        public Builder setShortName( String shortName )
        {
            this.shortName = shortName;

            return this;
        }

        public Builder setLongName( String longName )
        {
            this.longName = longName;

            return this;
        }

        public Builder setDescription( String description )
        {
            this.description = description;

            return this;
        }

        public Builder setTags( String...tags )
        {
            this.tags = tags;

            return this;
        }

        public Builder setContact( String contact )
        {
            this.contact = contact;

            return this;
        }

        public Builder setDeveloper( String developer )
        {
            this.developer = developer;

            return this;
        }

        public Builder setAttribution( String attribution )
        {
            this.attribution = attribution;

            return this;
        }

        public Builder setSyndicationRight( String syndicationRight )
        {
            this.syndicationRight = syndicationRight;

            return this;
        }

        public Builder setAdultContent( boolean adultContent )
        {
            this.adultContent = adultContent;

            return this;
        }

        public Builder setLanguage( Locale language )
        {
            this.language = language;

            return this;
        }

        public Builder setInputEncoding( Charset inputEncoding )
        {
            this.inputEncoding = inputEncoding;

            return this;
        }

        public Builder setOutputEncoding( Charset outputEncoding )
        {
            this.outputEncoding = outputEncoding;

            return this;
        }

        public Builder addUrlTemplate( String type, String stringUrlTemplate )
        {
            typeUrlTemplates.put( type, parseUrlTemplate( stringUrlTemplate ) );

            return this;
        }

        public CatalogueDescription build()
        {
            return new CatalogueDescription( shortName,
                                             longName,
                                             description,
                                             tags,
                                             contact,
                                             developer,
                                             attribution,
                                             syndicationRight,
                                             adultContent,
                                             language,
                                             inputEncoding,
                                             outputEncoding,
                                             typeUrlTemplates );
        }

    }
}
