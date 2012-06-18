package com.terradue.jcatalogue.repository;

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

import static com.terradue.jcatalogue.lang.Preconditions.checkArgument;

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
