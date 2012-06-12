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

import static com.terradue.jcatalogue.utils.Objects.checkArgument;

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

public final class HttpAuthentication
    implements Authentication
{

    public enum HttpAuthScheme
    {

        BASIC,
        DIGEST,
        KERBEROS,
        NONE,
        NTLM,
        SPNEGO

    }

    private final String username;

    private final char[] password;

    private boolean preemptive = false;

    private HttpAuthScheme httpAuthScheme = HttpAuthScheme.BASIC;

    public HttpAuthentication( String username, char[] password )
    {
        checkArgument( username != null, "Argument 'username' must be not null" );
        checkArgument( password != null, "Argument 'password' must be not null" );

        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public char[] getPassword()
    {
        return password;
    }

    public boolean isPreemptive()
    {
        return preemptive;
    }

    public void setPreemptive( boolean preemptive )
    {
        this.preemptive = preemptive;
    }

    public HttpAuthScheme getHttpAuthScheme()
    {
        return httpAuthScheme;
    }

    public void setHttpAuthScheme( HttpAuthScheme httpAuthScheme )
    {
        this.httpAuthScheme = httpAuthScheme;
    }

}
