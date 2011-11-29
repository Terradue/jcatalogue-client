package com.terradue.jcatalogue.client.converters;

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

import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class LocaleConverter
    implements Converter
{

    private static final String SEPARATOR = "-";

    public Object convert( @SuppressWarnings( "rawtypes" ) Class type, Object value )
    {
        if ( value == null )
        {
            throw new ConversionException( "Null values not supported in this version." );
        }

        if ( String.class == type )
        {
            if ( value instanceof Locale )
            {
                Locale locale = (Locale) value;
                return locale.getLanguage() + SEPARATOR + locale.getCountry();
            }
        }
        else if ( Locale.class == type )
        {
            if ( value instanceof String )
            {
                StringTokenizer tokenizer = new StringTokenizer( (String) value, SEPARATOR );
                return new Locale( tokenizer.nextToken(), tokenizer.nextToken() );
            }
        }
        throw new ConversionException( format( "type %s and value %s not supported", type, value ) );
    }

}
