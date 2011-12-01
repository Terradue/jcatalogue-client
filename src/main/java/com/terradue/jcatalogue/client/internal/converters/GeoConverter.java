package com.terradue.jcatalogue.client.internal.converters;

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

import static java.lang.String.format;
import static org.apache.commons.beanutils.ConvertUtils.lookup;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.terradue.jcatalogue.client.geo.Box;
import com.terradue.jcatalogue.client.geo.Line;
import com.terradue.jcatalogue.client.geo.Point;
import com.terradue.jcatalogue.client.geo.Polygon;

public final class GeoConverter
    implements Converter
{

    private static final String SEPARATOR = " ";

    private static final int EVEN_VERIFIER = 2;

    private final Converter doubleConverter = lookup( Double.class );

    public Object convert( @SuppressWarnings( "rawtypes" ) Class type, Object value )
    {
        if ( value == null )
        {
            throw new ConversionException( "Null values not supported in this version." );
        }

        if ( value instanceof String )
        {
            String stringValue = (String) value;

            if ( stringValue.isEmpty() )
            {
                throw new ConversionException( "empty value cannot be converted to a valid GeoLocation" );
            }

            String[] values = ( stringValue ).split( SEPARATOR );

            if ( ( values.length % EVEN_VERIFIER ) != 0 )
            {
                throw new ConversionException( format( "Cannot convert %s to a Geo representation, coordinates must be even in number (%s)",
                                                       value, values.length ) );
            }

            // extract points
            Point[] points = new Point[values.length / EVEN_VERIFIER];

            for ( int i = 0; i < values.length; i += EVEN_VERIFIER )
            {
                Double latitude = (Double) doubleConverter.convert( Double.class, values[i] );
                Double longitude = (Double) doubleConverter.convert( Double.class, values[i+1] );

                points[i / EVEN_VERIFIER] = new Point( latitude, longitude );
            }

            if ( Box.class == type )
            {
                if ( points.length != EVEN_VERIFIER )
                {
                    throw new ConversionException( format( "Impossible to marshall to %s, requires % points (found %s)",
                                                           Box.class.getName(), EVEN_VERIFIER, points.length ) );
                }
                return new Box( points[0], points[1] );
            }
            else if ( Line.class == type )
            {
                return new Line( points );
            }
            else if ( Point.class == type )
            {
                return points[0];
            }
            else if ( Polygon.class == type )
            {
                if ( !points[0].equals( points[points.length - 1] ) )
                {
                    throw new ConversionException( "Input points don't descrive a valid Polygon, first and last Points are not equals" );
                }
                return new Polygon( points );
            }
        }
        throw new ConversionException( format( "type %s and value %s not supported", type, value ) );
    }

}
