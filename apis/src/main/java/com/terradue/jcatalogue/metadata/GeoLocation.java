package com.terradue.jcatalogue.metadata;

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

import static com.terradue.jcatalogue.utils.Objects.checkArgument;
import static java.util.Arrays.asList;

public abstract class GeoLocation
{

    /**
     * Point
     */
    public static final class Point
        extends GeoLocation
    {

        private final Double latitude;

        private final Double longitude;

        public Point( Double latitude, Double longitude )
        {
            checkArgument( latitude != null, "Argument 'latitude' must be not null" );
            checkArgument( longitude != null, "Argument 'longitude' must be not null" );
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude()
        {
            return latitude;
        }

        public Double getLongitude()
        {
            return longitude;
        }

    }

    /**
     * Box
     */
    public static final class Box
        extends GeoLocation
    {

        private final Point lowerCorner;

        private final Point upperCorner;

        public Box( Point lowerCorner, Point upperCorner )
        {
            checkArgument( lowerCorner != null, "Argument 'lowerCorner' must be not null" );
            checkArgument( upperCorner != null, "Argument 'upperCorner' must be not null" );
            this.lowerCorner = lowerCorner;
            this.upperCorner = upperCorner;
        }

        public Point getLowerCorner()
        {
            return lowerCorner;
        }

        public Point getUpperCorner()
        {
            return upperCorner;
        }

    }

    /**
     * Line
     */
    public static final class Line
        extends GeoLocation
    {

        private final Point[] points;

        public Line( Point...points )
        {
            checkArgument( points != null, "Argument 'points' must be not null" );
            this.points = points;
        }

        public Iterable<Point> getPoints()
        {
            return asList( points );
        }

    }

    /**
     * Polygon
     */
    public static final class Polygon
        extends GeoLocation
    {

        private final Point[] points;

        public Polygon( Point...points )
        {
            checkArgument( points != null, "Argument 'points' must be not null" );
            this.points = points;
        }

        public Iterable<Point> getPoints()
        {
            return asList( points );
        }

    }

}
