package com.terradue.jcatalogue.client;

import java.io.File;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode( callSuper = true )
public final class DataSet
    extends AtomEntity
{

    public void download( File targetDir )
    {
        getCatalogueClient().downloadFile( targetDir, getEnclosure() );
    }

}
