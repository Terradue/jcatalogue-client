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

import static com.terradue.jcatalogue.client.utils.Assertions.checkState;
import static java.net.HttpURLConnection.HTTP_OK;

import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.filter.FilterContext;
import com.ning.http.client.filter.FilterException;
import com.ning.http.client.filter.ResponseFilter;

/**
 * @since 0.8
 */
final class StatusResponseFilter
    implements ResponseFilter
{

    @Override
    @SuppressWarnings( "rawtypes" )
    public FilterContext filter( FilterContext ctx )
        throws FilterException
    {
        HttpResponseStatus responseStatus = ctx.getResponseStatus();

        checkState( HTTP_OK == responseStatus.getStatusCode(),
                    "Impossible to query the catalog %s, server replied %s",
                    ctx.getRequest().getRawUrl(), responseStatus.getStatusText() );

        return ctx;
    }

}
