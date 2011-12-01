package com.terradue.jcatalogue.client.geo;

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

import lombok.EqualsAndHashCode;

@EqualsAndHashCode( callSuper = true )
public final class Polygon
    extends GeoLocation
{

    private final Point[] points;

    public Polygon( Point[] points )
    {
        if ( !points[0].equals( points[points.length - 1] ) )
        {
            throw new IllegalArgumentException( "Input points don't descrive a valid Polygon, first and last Points are not equals" );
        }
        this.points = points;
    }

}
