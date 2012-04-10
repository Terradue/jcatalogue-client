package com.terradue.jcatalogue.transfer;

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
