package com.terradue.jcatalogue.transfer;

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

import com.terradue.jcatalogue.CatalogueException;

public final class ChecksumFailureException
    extends CatalogueException
{

    /**
     *
     */
    private static final long serialVersionUID = 5339407058573396440L;

    private final String expected;

    private final String actual;

    public ChecksumFailureException( String expected, String actual )
    {
        super( "Checksum validation failed, expected %s but is %s", expected, actual );
        this.expected = expected;
        this.actual = actual;
    }

    public String getExpected()
    {
        return expected;
    }

    public String getActual()
    {
        return actual;
    }

}
