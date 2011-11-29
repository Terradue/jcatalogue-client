package com.terradue.jcatalogue.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class CatalogueEntity
{

    @Getter( AccessLevel.PACKAGE )
    @Setter( AccessLevel.PACKAGE )
    private CatalogueClient catalogueClient;

}
