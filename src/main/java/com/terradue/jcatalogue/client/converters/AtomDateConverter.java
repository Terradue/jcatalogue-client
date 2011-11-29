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
import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static java.util.TimeZone.getTimeZone;
import static java.util.regex.Pattern.compile;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class AtomDateConverter
    implements Converter
{

    private static final Pattern PATTERN = compile(
    "(\\d{4})(?:-(\\d{2}))?(?:-(\\d{2}))?(?:[Tt](?:(\\d{2}))?(?::(\\d{2}))?(?::(\\d{2}))?(?:\\.(\\d{3}))?)?([Zz])?(?:([+-])(\\d{2}):(\\d{2}))?");

    public Object convert( @SuppressWarnings( "rawtypes" ) Class type, Object value )
    {
        if ( value == null )
        {
            throw new ConversionException( "Null values not supported in this version." );
        }

        if ( String.class == type )
        {
            if ( value instanceof Date )
            {
                StringBuilder sb = new StringBuilder();
                Calendar c = getInstance( getTimeZone( "GMT" ) );
                c.setTime( (Date) value );
                sb.append( c.get( YEAR ) );
                sb.append( '-' );
                int f = c.get( MONTH );
                if ( f < 9 )
                    sb.append( '0' );
                sb.append( f + 1 );
                sb.append( '-' );
                f = c.get( DATE );
                if ( f < 10 )
                    sb.append( '0' );
                sb.append( f );
                sb.append( 'T' );
                f = c.get( HOUR_OF_DAY );
                if ( f < 10 )
                    sb.append( '0' );
                sb.append( f );
                sb.append( ':' );
                f = c.get( MINUTE );
                if ( f < 10 )
                    sb.append( '0' );
                sb.append( f );
                sb.append( ':' );
                f = c.get( SECOND );
                if ( f < 10 )
                    sb.append( '0' );
                sb.append( f );
                sb.append( '.' );
                f = c.get( MILLISECOND );
                if ( f < 100 )
                    sb.append( '0' );
                if ( f < 10 )
                    sb.append( '0' );
                sb.append( f );
                sb.append( 'Z' );
                return sb.toString();
            }
        }
        else if ( Date.class == type )
        {
            if ( value instanceof String )
            {
                Matcher m = PATTERN.matcher( (String) value );
                if ( m.find() )
                {
                    Calendar c = Calendar.getInstance( getTimeZone( "GMT" ) );
                    int hoff = 0, moff = 0, doff = -1;
                    if ( m.group( 9 ) != null )
                    {
                        doff = m.group( 9 ).equals( "-" ) ? 1 : -1;
                        hoff = doff * ( m.group( 10 ) != null ? Integer.parseInt( m.group( 10 ) ) : 0 );
                        moff = doff * ( m.group( 11 ) != null ? Integer.parseInt( m.group( 11 ) ) : 0 );
                    }
                    c.set( Calendar.YEAR, Integer.parseInt( m.group( 1 ) ) );
                    c.set( Calendar.MONTH, m.group( 2 ) != null ? Integer.parseInt( m.group( 2 ) ) - 1 : 0 );
                    c.set( Calendar.DATE, m.group( 3 ) != null ? Integer.parseInt( m.group( 3 ) ) : 1 );
                    c.set( Calendar.HOUR_OF_DAY, m.group( 4 ) != null ? Integer.parseInt( m.group( 4 ) ) + hoff : 0 );
                    c.set( Calendar.MINUTE, m.group( 5 ) != null ? Integer.parseInt( m.group( 5 ) ) + moff : 0 );
                    c.set( Calendar.SECOND, m.group( 6 ) != null ? Integer.parseInt( m.group( 6 ) ) : 0 );
                    c.set( Calendar.MILLISECOND, m.group( 7 ) != null ? Integer.parseInt( m.group( 7 ) ) : 0 );
                    return c.getTime();
                }
                throw new IllegalArgumentException( "Invalid Date Format" );
            }
        }
        throw new ConversionException( format( "type %s and value %s not supported", type, value ) );
    }

}
