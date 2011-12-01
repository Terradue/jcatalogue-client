package com.terradue.jcatalogue.client;

/*
 *    Copyright 2011 Terradue srl
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

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = false )
public final class OpenSearchUrl
    extends CatalogueEntity
{

    private static final String APPLICATION_ATOM_XML = "application/atom+xml";

    private static final String PARAM_DELIMITER = "&";

    private static final String OPTIONAL_MARKER = "?";

    private static final Pattern PARAM_VALUE_PATTERN = compile( "\\{(([\\w:_]+)(\\?)?)\\}" );

    private final String type;

    private final String baseUrl;

    private final OpenSearchParameter[] parameters;

    public OpenSearchUrl( final String type, final String template )
    {
        this.type = type;

        int queryDelimiterIndex = template.indexOf( '?' );

        baseUrl = template.substring( 0, queryDelimiterIndex );

        StringTokenizer parametersTokenizer = new StringTokenizer( template.substring( queryDelimiterIndex + 1 ),
                                                                   PARAM_DELIMITER );
        parameters = new OpenSearchParameter[parametersTokenizer.countTokens()];
        int index = 0;

        while ( parametersTokenizer.hasMoreTokens() )
        {
            String parameter = parametersTokenizer.nextToken();
            int parameterSeparatorIndex = parameter.indexOf( '=' );

            String key = parameter.substring( 0, parameterSeparatorIndex );
            String value = parameter.substring( parameterSeparatorIndex + 1 );

            Matcher matcher = PARAM_VALUE_PATTERN.matcher( value );
            if ( matcher.matches() )
            {
                boolean mandatory = ( 4 == matcher.groupCount() && OPTIONAL_MARKER.equals( matcher.group( 3 ) ) );
                parameters[index] = new OpenSearchParameter( key, matcher.group( 2 ), mandatory );
            }
            else
            {
                // ignore?!?
            }

            index++;
        }
    }

    public Catalogue invoke( Parameter...parameters )
    {
        if ( !APPLICATION_ATOM_XML.equals( type ) )
        {
            throw new IllegalStateException( "Direct URL invocation supports application/atom+xml only" );
        }

        // check if there are missing parameters first
        Set<String> parametersName = new HashSet<String>();
        for ( Parameter parameter : parameters )
        {
            parametersName.add( parameter.getName() );
        }

        for ( OpenSearchParameter searchParameter : this.parameters )
        {
            if ( searchParameter.isMandatory() && !parametersName.contains( searchParameter.getKey() ) )
            {
                throw new IllegalArgumentException( format( "Mandatory parameter %s not found!", searchParameter.getKey() ) );
            }
        }

        return getCatalogueClient().getCatalogue( baseUrl, parameters );
    }

}
