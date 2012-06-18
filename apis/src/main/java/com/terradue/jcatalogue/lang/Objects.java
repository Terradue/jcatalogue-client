package com.terradue.jcatalogue.lang;

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

    /**
     * Computes a hashCode given the input objects.
     *
     * @param initialNonZeroOddNumber a non-zero, odd number used as the initial value.
     * @param multiplierNonZeroOddNumber a non-zero, odd number used as the multiplier.
     * @param objs the objects to compute hash code.
     * @return the computed hashCode.
     */
    public static int hash( int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object...objs )
    {
        int result = initialNonZeroOddNumber;
        for ( Object obj : objs )
        {
            result = multiplierNonZeroOddNumber * result + ( obj != null ? obj.hashCode() : 0 );
        }
        return result;
    }

}
