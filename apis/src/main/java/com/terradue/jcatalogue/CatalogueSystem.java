package com.terradue.jcatalogue;

import com.terradue.jcatalogue.metadata.CatalogueDescription;
import com.terradue.jcatalogue.transfer.MetadataNotFoundException;
import com.terradue.jcatalogue.transfer.MetadataTransferException;

public interface CatalogueSystem
{

    CatalogueDescription resolveDescription( CatalogueSystemSession session )
        throws MetadataNotFoundException, MetadataTransferException;

}
