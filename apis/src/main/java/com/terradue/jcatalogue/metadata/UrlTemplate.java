package com.terradue.jcatalogue.metadata;

import static com.terradue.jcatalogue.utils.Objects.checkArgument;
import static java.util.regex.Pattern.compile;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UrlTemplate
{

    private static final String PARAM_DELIMITER = "&";

    private static final String OPTIONAL_MARKER = "?";

    private static final Pattern PARAM_VALUE_PATTERN = compile( "\\{(([\\w:_]+)(\\?)?)\\}" );

    public static UrlTemplate parseUrlTemplate( String template )
    {
        int queryDelimiterIndex = template.indexOf( '?' );

        String baseUrl = template.substring( 0, queryDelimiterIndex );

        StringTokenizer parametersTokenizer = new StringTokenizer( template.substring( queryDelimiterIndex + 1 ),
                                                                   PARAM_DELIMITER );
        UrlTemplateParameter[] templateParameters = new UrlTemplateParameter[parametersTokenizer.countTokens()];
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
                templateParameters[index] = new UrlTemplateParameter( key, matcher.group( 2 ), mandatory );
            }
            else
            {
                // ignore?!?
            }

            index++;
        }

        return new UrlTemplate( baseUrl, templateParameters );
    }

    private final String baseUrl;

    private final UrlTemplateParameter[] templateParameters;

    public UrlTemplate( String baseUrl, UrlTemplateParameter...templateParameters )
    {
        checkArgument( baseUrl != null, "Argument 'baseUrl' must be not null" );
        checkArgument( templateParameters != null, "Argument 'templateParameters' must be not null" );

        this.baseUrl = baseUrl;
        this.templateParameters = templateParameters;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public UrlTemplateParameter[] getTemplateParameters()
    {
        return templateParameters;
    }

    public static final class UrlTemplateParameter
    {

        private final String key;

        private final String type;

        private final boolean mandatory;

        public UrlTemplateParameter( String key, String type, boolean mandatory )
        {
            checkArgument( key != null, "Argument 'key' must be not null" );
            checkArgument( type != null, "Argument 'type' must be not null" );

            this.key = key;
            this.type = type;
            this.mandatory = mandatory;
        }

        public String getKey()
        {
            return key;
        }

        public String getType()
        {
            return type;
        }

        public boolean isMandatory()
        {
            return mandatory;
        }

    }

}
