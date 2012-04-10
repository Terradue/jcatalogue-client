package com.terradue.jcatalogue;

import java.net.URL;

import com.terradue.jcatalogue.repository.RemoteRepository;

public class AAA
{

    public void repo()
        throws Exception
    {
        RemoteRepository catalogue = new RemoteRepository( "va4", "va4.esa", new URL( "http://eo-virtual-archive4.esa.int" ) );
    }

}
