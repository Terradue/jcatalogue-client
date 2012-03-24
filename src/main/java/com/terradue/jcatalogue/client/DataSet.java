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

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.terradue.jcatalogue.client.download.DownloadHandler;
import com.terradue.jcatalogue.client.geo.GeoLocation;

@Data
@EqualsAndHashCode( callSuper = true )
public final class DataSet
    extends AtomEntity
{

    private static final Logger logger = LoggerFactory.getLogger( DataSet.class );

    private Date beginPosition;

    private Date endPosition;

    private GeoLocation geoLocation;

    public <T> T download( File targetDir, DownloadHandler<T> handler )
    {
        if ( logger.isInfoEnabled() )
        {
            logger.info( "Downloading DataSet {} media file...", getId() );
        }

        return getCatalogueClient().downloadFile( targetDir, getEnclosures(), handler );
    }

}
