package com.terradue.jcatalogue.transfer;

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
import java.net.URI;

/**
 * Describes a resource being uploaded or downloaded by the repository system.
 */
public final class TransferResource
{

    private final URI repositoryUrl;

    private final String resourceName;

    private final File file;

    private final long startTime;

    private long contentLength = -1;

    /**
     * Creates a new transfer resource with the specified properties.
     *
     * @param repositoryUrl The base URI of the repository, may be {@code null} or empty if unknown. If not empty, a
     *            trailing slash will automatically be added if missing.
     * @param resourceName The relative path to the resource within the repository, may be {@code null}. A leading slash
     *            (if any) will be automatically removed.
     * @param file The source/target file involved in the transfer, may be {@code null}.
     * @param trace The trace information, may be {@code null}.
     */
    public TransferResource( URI repositoryUrl, String resourceName, File file )
    {
        this.repositoryUrl = repositoryUrl;

        if ( resourceName == null || resourceName.length() <= 0 )
        {
            this.resourceName = "";
        }
        else if ( resourceName.startsWith( "/" ) )
        {
            this.resourceName = resourceName.substring( 1 );
        }
        else
        {
            this.resourceName = resourceName;
        }

        this.file = file;

        startTime = System.currentTimeMillis();
    }

    /**
     * The base URI of the repository, e.g. "http://repo1.maven.org/maven2/". Unless the URI is unknown, it will be
     * terminated by a trailing slash.
     *
     * @return The base URI of the repository or an empty string if unknown, never {@code null}.
     */
    public URI getRepositoryUrl()
    {
        return repositoryUrl;
    }

    /**
     * The path of the resource relative to the repository's base URI, e.g. "org/apache/maven/maven/3.0/maven-3.0.pom".
     *
     * @return The path of the resource, never {@code null}.
     */
    public String getResourceName()
    {
        return resourceName;
    }

    /**
     * Gets the local file being uploaded or downloaded. When the repository system merely checks for the existence of a
     * remote resource, no local file will be involved in the transfer.
     *
     * @return The source/target file involved in the transfer or {@code null} if none.
     */
    public File getFile()
    {
        return file;
    }

    /**
     * The size of the resource in bytes. Note that the size of a resource during downloads might be unknown to the
     * client which is usually the case when transfers employ compression like gzip. In general, the content length is
     * not known until the transfer has {@link TransferListener#transferStarted(TransferEvent) started}.
     *
     * @return The size of the resource in bytes or a negative value if unknown.
     */
    public long getContentLength()
    {
        return contentLength;
    }

    /**
     * Sets the size of the resource in bytes.
     *
     * @param contentLength The size of the resource in bytes or a negative value if unknown.
     * @return This resource for chaining, never {@code null}.
     */
    public TransferResource setContentLength( long contentLength )
    {
        this.contentLength = contentLength;
        return this;
    }

    /**
     * Gets the timestamp when the transfer of this resource was started.
     *
     * @return The timestamp when the transfer of this resource was started.
     */
    public long getTransferStartTime()
    {
        return startTime;
    }

    @Override
    public String toString()
    {
        return getRepositoryUrl() + getResourceName() + " <> " + getFile();
    }

}
