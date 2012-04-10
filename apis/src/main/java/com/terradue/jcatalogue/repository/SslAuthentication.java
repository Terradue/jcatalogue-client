package com.terradue.jcatalogue.repository;

import static com.terradue.jcatalogue.utils.Objects.checkArgument;

import java.io.File;

public final class SslAuthentication
    implements Authentication
{

    private final File key;

    private final File certificate;

    private final char[] password;

    public SslAuthentication( File proxyCertificate )
    {
        this( proxyCertificate, proxyCertificate, "".toCharArray() );
    }

    public SslAuthentication( File key, File certificate, char[] password )
    {
        checkArgument( key != null, "Argument 'key' must be not null" );
        checkArgument( certificate != null, "Argument 'certificate' must be not null" );
        checkArgument( password != null, "Argument 'password' must be not null" );

        this.key = key;
        this.certificate = certificate;
        this.password = password;
    }

    public File getKey()
    {
        return key;
    }

    public File getCertificate()
    {
        return certificate;
    }

    public char[] getPassword()
    {
        return password;
    }

}
