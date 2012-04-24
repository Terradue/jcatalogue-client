package com.terradue.jcatalogue.client.internal.collections;

/*
 *    Copyright 2011-2012 Terradue srl
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

import java.net.URI;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

public final class Enclosures
    implements Comparator<Integer>, Iterable<URI>
{

    private final Map<Integer, Set<URI>> enclosures = new TreeMap<Integer, Set<URI>>( this );

    public void addEnclosure( URI uri, Integer priority )
    {
        Set<URI> enclosuresSet = enclosures.get( priority );

        if ( enclosuresSet == null )
        {
            enclosuresSet = new HashSet<URI>();
            enclosures.put( priority, enclosuresSet );
        }

        enclosuresSet.add( uri );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<URI> iterator()
    {
        return new Iterator<URI>()
        {

            private Iterator<Integer> keys = enclosures.keySet().iterator();

            private Iterator<URI> pending = null;

            private URI next = null;

            @Override
            public boolean hasNext()
            {
                if ( next != null )
                {
                    return true;
                }

                while ( ( pending == null ) || !pending.hasNext() )
                {
                    if ( !keys.hasNext() )
                    {
                        return false;
                    }
                    pending = enclosures.get( keys.next() ).iterator();
                }

                next = pending.next();
                return true;
            }

            @Override
            public URI next()
            {
                if ( !hasNext() )
                {
                    throw new NoSuchElementException();
                }
                URI returned = next;
                next = null;
                return returned;
            }

            @Override
            public void remove()
            {
                pending.remove();
            }

        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare( Integer o1, Integer o2 )
    {
        return o2.compareTo( o1 );
    }

}
