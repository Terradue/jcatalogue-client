package com.terradue.jcatalogue.client;

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

import com.ning.http.client.Realm.AuthScheme;

public enum HttpAuthScheme
{

    BASIC( AuthScheme.BASIC ),
    DIGEST( AuthScheme.DIGEST ),
    KERBEROS( AuthScheme.KERBEROS ),
    NONE( AuthScheme.NONE ),
    NTLM( AuthScheme.NTLM ),
    SPNEGO( AuthScheme.SPNEGO );

    private final AuthScheme authScheme;

    HttpAuthScheme( AuthScheme authScheme )
    {
        this.authScheme = authScheme;
    }

    public AuthScheme getAuthScheme()
    {
        return authScheme;
    }

}
