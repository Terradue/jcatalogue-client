package com.terradue.jcatalogue.client.internal.ahc;

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

import static com.ning.http.util.AsyncHttpProviderUtils.parseCookie;
import static com.terradue.jcatalogue.client.internal.lang.Assertions.checkState;
import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.net.URI;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.Cookie;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.filter.FilterContext;
import com.ning.http.client.filter.FilterException;
import com.ning.http.client.filter.ResponseFilter;
import com.terradue.jcatalogue.client.HttpMethod;
import com.terradue.jcatalogue.client.Parameter;

/**
 * @since 0.8
 */
final class UmSsoStatusResponseFilter
    implements ResponseFilter
{

    private static final String SET_COOKIE = "Set-Cookie";

    private static final String CONTENT_TYPE = "Content-Type";

    private final Pattern textHtmlPattern = Pattern.compile( "text/html", CASE_INSENSITIVE );

    private final BitSet admittedStatuses = new BitSet();

    private final Logger logger = LoggerFactory.getLogger( getClass() );

    private final Map<String, UmSsoAccess> umSsoCredentials;

    private final Map<String, Map<String, Cookie>> cookiesRegistry;

    public UmSsoStatusResponseFilter( Map<String, UmSsoAccess> umSsoCredentials,
                                      Map<String, Map<String, Cookie>> cookiesRegistry)
    {
        this.umSsoCredentials = umSsoCredentials;
        this.cookiesRegistry = cookiesRegistry;

        admittedStatuses.set( HTTP_OK );
        admittedStatuses.set( HTTP_MOVED_PERM );
        admittedStatuses.set( HTTP_MOVED_TEMP );
        admittedStatuses.set( 307 ); // Temporary Redirect
    }

    @Override
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    public FilterContext filter( FilterContext ctx )
        throws FilterException
    {
        HttpResponseStatus responseStatus = ctx.getResponseStatus();

        // 1. verify the state is one of the admitted
        checkState( admittedStatuses.get( responseStatus.getStatusCode() ),
                    "Impossible to query the catalog %s, server replied %s",
                    ctx.getRequest().getRawUrl(), responseStatus.getStatusText() );

        final String currentDomain = URI.create( ctx.getRequest().getUrl() ).getHost();

        // 2. collect all the cookies, indexing them per domain

        final List<String> cookiesString = ctx.getResponseHeaders().getHeaders().get( SET_COOKIE );
        final List<Cookie> cookies = new LinkedList<Cookie>();

        // just log there are no cookies to manage for the current domain
        if ( cookiesString == null || cookiesString.isEmpty() )
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "No cookies to manage in domain {}", currentDomain );
            }
        }
        else
        {
            // verify there is a cookies index for the current domain
            Map<String, Cookie> domainCookies = cookiesRegistry.get( currentDomain );
            if ( domainCookies == null )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Domain {} was not already managed", currentDomain );
                }

                // create the index and store it
                domainCookies = new HashMap<String, Cookie>();
                cookiesRegistry.put( currentDomain, domainCookies );
            }

            // collect all cookies, adding/replacing them with latest updated value
            for ( String cookieValue : cookiesString )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Received cookie {}", cookieValue );
                }

                Cookie currentCookie = parseCookie( cookieValue );

                cookies.add( currentCookie );
                domainCookies.put( currentCookie.getName(), currentCookie );
            }
        }

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Checking server status code reply: {}", responseStatus.getStatusCode() );
        }

        // check UM-SSO conditions - this is hack-ish but didn't find a better solution ATM
        if ( HTTP_OK == responseStatus.getStatusCode() )
        {
            String contentType = ctx.getResponseHeaders().getHeaders().getFirstValue( CONTENT_TYPE );

            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Checking content type {} behavior", contentType );
            }

            if ( !textHtmlPattern.matcher( contentType ).matches() )
            {
                logger.debug( "Content type {} doesn't ned to be analyzed, just proceed", contentType );

                // return ctx;
            }

            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Checking UM-SSO auth credentials for current host {}", currentDomain );
            }

            UmSsoAccess umSsoAccess = umSsoCredentials.get( currentDomain );

            if ( umSsoAccess != null )
            {
                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Redirecting request to {} {}",
                                  umSsoAccess.getHttpMethod(), umSsoAccess.getLoginFormUrl() );
                }

                RequestBuilder authRequestBuilder = new RequestBuilder( umSsoAccess.getHttpMethod().toString() )
                                                             .setUrl( umSsoAccess.getLoginFormUrl().toString() );

                for ( Cookie cookie : cookiesRegistry.get( currentDomain ).values() )
                {
                    if ( logger.isDebugEnabled() )
                    {
                        logger.debug( "Adding {} for host {}", cookie, currentDomain );
                    }

                    authRequestBuilder.addCookie( cookie );
                }

                for ( Parameter parameter : umSsoAccess.getParameters() )
                {
                    authRequestBuilder.addParameter( parameter.getName(), parameter.getValue() );
                }

                return new FilterContext.FilterContextBuilder( ctx )
                            .request( authRequestBuilder.build() )
                            .asyncHandler( ctx.getAsyncHandler() )
                            .replayRequest( true )
                            .build();
            }
            else if ( logger.isWarnEnabled() )
            {
                logger.warn( "Domain {} not managed for UM-SSO authentication!", currentDomain );
            }
        }

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Proceeding on serving the request" );
        }

        if ( ctx.getResponseStatus().getStatusCode() > 300 // if it is a redirect
                        && HttpMethod.POST.toString().equals( ctx.getRequest().getMethod() ) )
        {
            RequestBuilder redirect = new RequestBuilder( ctx.getRequest() )
                .setMethod( HttpMethod.GET.toString() );

            // reply all cookies
            for ( Cookie cookie : cookies )
            {
                redirect.addOrReplaceCookie( cookie );
            }

            return new FilterContext.FilterContextBuilder( ctx )
                .request( redirect.build() )
                .replayRequest( true )
                .build();
        }

        return ctx;
    }

}
