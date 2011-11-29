package com.terradue.jcatalogue.client.converters;

import static java.lang.String.format;
import static java.nio.charset.Charset.forName;

import java.nio.charset.Charset;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class CharsetConverter
    implements Converter
{

    public Object convert( @SuppressWarnings( "rawtypes" ) Class type, Object value )
    {
        if ( value == null )
        {
            throw new ConversionException( "Null values not supported in this version." );
        }

        if ( String.class == type )
        {
            if ( value instanceof Charset )
            {
                return ( (Charset) value).displayName();
            }
        }
        else if ( Charset.class == type )
        {
            if ( value instanceof String )
            {
                return forName( (String) value );
            }
        }
        throw new ConversionException( format( "type %s and value %s not supported", type, value ) );
    }

}
