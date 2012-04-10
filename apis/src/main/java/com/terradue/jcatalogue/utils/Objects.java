package com.terradue.jcatalogue.utils;

import static java.lang.String.format;

public final class Objects
{

    private Objects()
    {
        // do nothing
    }

    public static <T> boolean eq( T s1, T s2 )
    {
        return s1 != null ? s1.equals( s2 ) : s2 == null;
    }

    public static int hash( Object obj )
    {
        return obj != null ? obj.hashCode() : 0;
    }

    public static void checkArgument( boolean expression, String errorMessagePattern, Object...args )
    {
        if ( !expression )
        {
            throw new IllegalArgumentException( format( errorMessagePattern, args ) );
        }
    }

    public static void checkState( boolean expression, String errorMessagePattern, Object...args )
    {
        if ( !expression )
        {
            throw new IllegalStateException( format( errorMessagePattern, args ) );
        }
    }

    public static <T> T checkNotNull( T reference, String errorMessagePattern, Object...args )
    {
        if ( reference == null )
        {
            throw new NullPointerException( format( errorMessagePattern, args ) );
        }
        return reference;
    }

}
