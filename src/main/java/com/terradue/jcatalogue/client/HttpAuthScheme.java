package com.terradue.jcatalogue.client;

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
