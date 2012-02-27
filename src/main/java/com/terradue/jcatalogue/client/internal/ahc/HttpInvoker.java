package com.terradue.jcatalogue.client.internal.ahc;

import static com.terradue.jcatalogue.client.internal.lang.Assertions.checkArgument;
import static com.terradue.jcatalogue.client.internal.lang.Assertions.checkNotNull;
import static java.util.Arrays.asList;

import java.io.File;
import java.net.URI;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.ssl.KeyMaterial;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Cookie;
import com.ning.http.client.Realm;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.resumable.ResumableIOExceptionFilter;
import com.terradue.jcatalogue.client.HttpAuthScheme;
import com.terradue.jcatalogue.client.HttpMethod;
import com.terradue.jcatalogue.client.Parameter;

/**
 * @since 0.8
 */
public final class HttpInvoker
{

    private final Map<String, Realm> realms = new HashMap<String, Realm>();

    private final List<KeyManager> keyManagers = new ArrayList<KeyManager>();

    private final TrustManager[] trustManagers = new TrustManager[] { new DummyTrustManager() };

    private final Map<String, Map<String, Cookie>> cookiesRegistry = new HashMap<String, Map<String, Cookie>>();

    private final Map<String, UmSsoAccess> umSsoCredentials = new HashMap<String, UmSsoAccess>();

    private final AsyncHttpClient httpClient;

    public HttpInvoker()
    {
        SSLContext context = null;
        try
        {
            context = SSLContext.getInstance( "TLS" );
            context.init( new KeyManager[] {}, trustManagers, null );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "Impossible to initialize SSL context", e );
        }

        httpClient = new AsyncHttpClient( new AsyncHttpClientConfig.Builder()
                                                .setAllowPoolingConnection( true )
                                                .addIOExceptionFilter( new ResumableIOExceptionFilter() )
                                                .setMaximumConnectionsPerHost( 10 )
                                                .setMaximumConnectionsTotal( 100 )
                                                .addResponseFilter( new UmSsoStatusResponseFilter( umSsoCredentials,
                                                                                                   cookiesRegistry ) )
                                                .setFollowRedirects( true )
                                                .setMaximumNumberOfRedirects( Integer.MAX_VALUE ) // thanks ESA guys
                                                .setSSLContext( context )
                                                .build() );
    }

    public <T> T invoke( HttpMethod httpMethod, URI uri, AsyncHandler<T> handler, Parameter...parameters )
    {
        checkNotNull( uri, "Input URI cannot be null" );

        RequestBuilder requestBuilder = new RequestBuilder( httpMethod.toString() ).setUrl( uri.toString() );

        // send all domain cookies, if any
        Map<String, Cookie> domainCookies = cookiesRegistry.get( uri.getHost() );
        if ( domainCookies != null && !domainCookies.isEmpty() )
        {
            for ( Cookie cookie : domainCookies.values() )
            {
                requestBuilder.addCookie( cookie );
            }
        }

        // send all parameters
        for ( Parameter parameter : parameters )
        {
            if ( HttpMethod.GET == httpMethod )
            {
                requestBuilder.addQueryParameter( parameter.getName(), parameter.getValue() );
            }
            else
            {
                requestBuilder.addParameter( parameter.getName(), parameter.getValue() );
            }
        }

        // send the realms authentication, if any
        if ( realms.containsKey( uri.getHost() ) )
        {
            requestBuilder.setRealm( realms.get( uri.getHost() ) );
        }

        try
        {
            return httpClient.executeRequest( requestBuilder.build(), handler ).get();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "An error occurred while invoking " + uri, e );
        }
    }

    public void registerRealm( String host, String username, String password, boolean preemptive, HttpAuthScheme authScheme )
    {
        host = checkNotNull( host, "host cannot be null" );
        username = checkNotNull( username, "username cannot be null" );
        password = checkNotNull( password, "password cannot be null" );
        authScheme = checkNotNull( authScheme, "authScheme cannot be null" );

        realms.put( host, new Realm.RealmBuilder()
                                   .setPrincipal( username )
                                   .setPassword( password )
                                   .setUsePreemptiveAuth( preemptive )
                                   .setScheme( authScheme.getAuthScheme() )
                                   .build() );
    }

    public void registerSSLProxy( File proxyCertificate )
    {
        registerSSLCerificates( proxyCertificate, proxyCertificate, null );
    }

    /**
     * @since 0.7
     */
    public void registerSSLCerificates( File sslCertificate, File sslKey, String sslPassword )
    {
        checkFile( sslCertificate );
        checkFile( sslKey );

        if ( sslPassword == null )
        {
            sslPassword = "";
        }

        final char[] password = sslPassword.toCharArray();

        try
        {
            final KeyStore store = new KeyMaterial( sslCertificate, sslKey, password ).getKeyStore();
            store.load( null, password );

            // initialize key and trust managers -> default behavior
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance( "SunX509" );
            // password for key and store have to be the same IIRC
            keyManagerFactory.init( store, password );

            keyManagers.addAll( asList( keyManagerFactory.getKeyManagers() ) );

            httpClient.getConfig().getSSLContext().init( keyManagers.toArray( new KeyManager[keyManagers.size()] ),
                                                         trustManagers,
                                                         null );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "Impossible to initialize SSL certificate/key", e );
        }
    }

    private static void checkFile( File file )
    {
        checkArgument( file.exists(), "File %s not found, please verify it exists", file );
        checkArgument( !file.isDirectory(), "File %s must be not a directory", file );
    }

    public void registerUmSsoAccess( String loginFormUrl, HttpMethod httpMethod, Parameter...parameters )
    {
        loginFormUrl = checkNotNull( loginFormUrl, "loginFormUrl cannot be null" );

        registerUmSsoCredentials( URI.create( loginFormUrl ), httpMethod, parameters );
    }

    public void registerUmSsoCredentials( URI loginFormUrl, HttpMethod httpMethod, Parameter...parameters )
    {
        loginFormUrl = checkNotNull( loginFormUrl, "loginFormUrl cannot be null" );
        httpMethod = checkNotNull( httpMethod, "httpMethod cannot be null" );
        parameters = checkNotNull( parameters, "loginFormUrl cannot be null" );

        umSsoCredentials.put( loginFormUrl.getHost(), new UmSsoAccess( loginFormUrl, httpMethod, parameters ) );
    }

    public void shutDown()
    {
        httpClient.close();
    }

}
