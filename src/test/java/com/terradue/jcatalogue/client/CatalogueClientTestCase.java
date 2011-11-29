package com.terradue.jcatalogue.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public final class CatalogueClientTestCase
{

    private CatalogueClient client;

    @Before
    public void setUp()
    {
        client = new CatalogueClient();
    }

    @After
    public void tearDown()
    {
        client = null;
    }

    @Test
    public void discoverCatalogDescription()
        throws Exception
    {
        CatalogueDescription catalogueDescription = client.discover( "http://eo-virtual-archive4.esa.int/search/description" );

        assertNotNull( catalogueDescription );
    }

    @Test
    public void accessToCatalogViaDescription()
        throws Exception
    {
        CatalogueDescription catalogueDescription = client.discover( "http://eo-virtual-archive4.esa.int/search/description" );
        Catalogue catalogue = catalogueDescription.getCatalogue();

        assertNotNull( catalogue );
    }

    @Test
    public void cannotAccessToCatalogPagination()
    {
        CatalogueDescription catalogueDescription = client.discover( "http://eo-virtual-archive4.esa.int/search/description" );
        Catalogue catalogue = catalogueDescription.getCatalogue();

        assertFalse( catalogue.hasMoreResults() );
    }

    @Test
    @Ignore
    public void traverseCatalogue()
    {
        // get the catalogue

        CatalogueDescription catalogueDescription = client.discover( "http://eo-virtual-archive4.esa.int/search/description" );
        Catalogue catalogue = catalogueDescription.getCatalogue();

        // series

        Iterator<Serie> seriesIterator = catalogue.iterator();

        assertTrue( seriesIterator.hasNext() );

        Serie serie = seriesIterator.next();

        assertNotNull( serie );

        // dataset

        Iterator<DataSet> dataSetIterator = serie.iterator();

        assertTrue( dataSetIterator.hasNext() );

        DataSet dataSet = dataSetIterator.next();

        assertNotNull( dataSet );

        // dowload the DataSet

        dataSet.download( new File( "/tmp" ) );
    }

}
