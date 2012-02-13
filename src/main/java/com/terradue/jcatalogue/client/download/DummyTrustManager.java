package com.terradue.jcatalogue.client.download;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

final class DummyTrustManager
    implements X509TrustManager
{

    @Override
    public void checkClientTrusted( X509Certificate[] certificates, String authType )
        throws CertificateException
    {
        // do nothing
    }

    @Override
    public void checkServerTrusted( X509Certificate[] certificates, String authType )
        throws CertificateException
    {
        // TODO do nothing
    }

    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return new X509Certificate[0];
    }

}
