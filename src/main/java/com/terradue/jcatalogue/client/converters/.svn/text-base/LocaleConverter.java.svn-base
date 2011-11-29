package com.terradue.jcatalogue.client.converters;

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
