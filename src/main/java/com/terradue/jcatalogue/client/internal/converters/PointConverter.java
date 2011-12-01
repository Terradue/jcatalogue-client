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

import java.util.StringTokenizer;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.terradue.jcatalogue.client.geo.Point;

public final class PointConverter
    implements Converter
{

    private static final String SEPARATOR = " ";

    private final Converter doubleConverter = lookup( Double.class );

    public Object convert( @SuppressWarnings( "rawtypes" ) Class type, Object value )
    {
        if ( value == null )
        {
            throw new ConversionException( "Null values not supported in this version." );
        }

        if ( Point.class == type )
        {
            if ( value instanceof String )
            {
                StringTokenizer tokenizer = new StringTokenizer( (String) value, SEPARATOR );

                Double latitude = (Double) doubleConverter.convert( Double.class, tokenizer.nextToken() );
                Double longitude = (Double) doubleConverter.convert( Double.class, tokenizer.nextToken() );

                return new Point( latitude.doubleValue(), longitude.doubleValue() );
            }
        }
        throw new ConversionException( format( "type %s and value %s not supported", type, value ) );
    }

}
