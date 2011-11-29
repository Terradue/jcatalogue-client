package com.terradue.jcatalogue.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode( callSuper = true )
public class Serie
    extends AtomEntity
    implements Iterable<DataSet>
{

    private final List<DataSet> dataSets = new ArrayList<DataSet>();

    public void addDataSet( DataSet dataSet )
    {
        dataSets.add( dataSet );
    }

    @Override
    public Iterator<DataSet> iterator()
    {
        return dataSets.iterator();
    }

    public Serie getNextResults()
    {
        if ( !hasMoreResults() )
        {
            throw new IllegalStateException( "More results not available" );
        }

        return getCatalogueClient().getSerie( getNextResultsUri() );
    }

    @Override
    void setCatalogueClient( CatalogueClient catalogueClient )
    {
        super.setCatalogueClient( catalogueClient );

        for ( DataSet dataSet : dataSets )
        {
            dataSet.setCatalogueClient( catalogueClient );
        }
    }

}
