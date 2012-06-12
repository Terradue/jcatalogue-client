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

import static com.terradue.jcatalogue.utils.Objects.checkArgument;

import java.nio.ByteBuffer;

import com.terradue.jcatalogue.CatalogueSystemSession;

/**
 * An event fired to a transfer listener during an artifact/metadata transfer.
 *
 * @see TransferListener
 * @see TransferEvent.Builder
 */
public final class TransferEvent
{

    /**
     * The type of the request/transfer being performed.
     */
    public enum RequestType
    {

        /**
         * Download product/metadata.
         */
        GET,

        /**
         * Upload product/metadata.
         */
        PUT,

    }

    private final RequestType requestType;

    private final CatalogueSystemSession session;

    private final TransferResource resource;

    private final ByteBuffer dataBuffer;

    private final long transferredBytes;

    private final Exception exception;

    private TransferEvent( RequestType requestType,
                           CatalogueSystemSession session,
                           TransferResource resource,
                           ByteBuffer dataBuffer,
                           long transferredBytes,
                           Exception exception )
    {
        this.requestType = requestType;
        this.session = session;
        this.resource = resource;
        this.dataBuffer = dataBuffer;
        this.transferredBytes = transferredBytes;
        this.exception = exception;
    }

    /**
     * Gets the type of the request/transfer.
     *
     * @return The type of the request/transfer, never {@code null}.
     */
    public RequestType getRequestType()
    {
        return requestType;
    }

    /**
     * Gets the repository system session during which the event occurred.
     *
     * @return The repository system session during which the event occurred, never {@code null}.
     */
    public CatalogueSystemSession getSession()
    {
        return session;
    }

    /**
     * Gets the resource that is being transferred.
     *
     * @return The resource being transferred, never {@code null}.
     */
    public TransferResource getResource()
    {
        return resource;
    }

    /**
     * Gets the total number of bytes that have been transferred since the download/upload was started.
     *
     * @return The total number of bytes that have been transferred since the transfer started, never negative.
     * @see #getDataLength()
     */
    public long getTransferredBytes()
    {
        return transferredBytes;
    }

    /**
     * Gets the byte buffer holding the transferred bytes since the last event. A listener must assume this buffer to be
     * owned by the event source and must not change any byte in this buffer. Also, the buffer is only valid for the
     * duration of the event callback, i.e. the next event might reuse the same buffer (with updated contents).
     * Therefore, if the actual event processing is deferred, the byte buffer would have to be cloned to create an
     * immutable snapshot of its contents.
     *
     * @return The (read-only) byte buffer or {@code null} if not applicable to the event, i.e. if the event type is not
     *         {@link EventType#PROGRESSED}.
     */
    public ByteBuffer getDataBuffer()
    {
        return ( dataBuffer != null ) ? dataBuffer.asReadOnlyBuffer() : null;
    }

    /**
     * Gets the number of bytes that have been transferred since the last event.
     *
     * @return The number of bytes that have been transferred since the last event, possibly zero but never negative.
     * @see #getTransferredBytes()
     */
    public int getDataLength()
    {
        return ( dataBuffer != null ) ? dataBuffer.remaining() : 0;
    }

    /**
     * Gets the error that occurred during the transfer.
     *
     * @return The error that occurred or {@code null} if none.
     */
    public Exception getException()
    {
        return exception;
    }

    @Override
    public String toString()
    {
        return getRequestType() + " " + getResource();
    }

    /**
     * A builder to create transfer events.
     */
    public static final class Builder
    {

        CatalogueSystemSession session;

        RequestType requestType;

        TransferResource resource;

        ByteBuffer dataBuffer;

        long transferredBytes;

        Exception exception;

        /**
         * Creates a new transfer event builder for the specified session and the given resource.
         *
         * @param session The repository system session, must not be {@code null}.
         * @param resource The resource being transferred, must not be {@code null}.
         */
        public Builder( CatalogueSystemSession session, TransferResource resource )
        {
            checkArgument( session != null, "Argument 'session' must be not null" );
            checkArgument( resource != null, "Argument 'resource' must be not null" );

            this.session = session;
            this.resource = resource;
            requestType = RequestType.GET;
        }

        private Builder( Builder prototype )
        {
            session = prototype.session;
            resource = prototype.resource;
            requestType = prototype.requestType;
            dataBuffer = prototype.dataBuffer;
            transferredBytes = prototype.transferredBytes;
            exception = prototype.exception;
        }

        /**
         * Creates a new transfer event builder from the current values of this builder. The state of this builder
         * remains unchanged.
         *
         * @return The new event builder, never {@code null}.
         */
        public Builder copy()
        {
            return new Builder( this );
        }

        /**
         * Sets the type of the request/transfer.
         *
         * @param requestType The request/transfer type, must not be {@code null}.
         * @return This event builder for chaining, never {@code null}.
         */
        public Builder setRequestType( RequestType requestType )
        {
            checkArgument( requestType != null, "Argument 'requestType' must be not null" );
            this.requestType = requestType;
            return this;
        }

        /**
         * Sets the total number of bytes that have been transferred so far during the download/upload.
         *
         * @param transferredBytes The total number of bytes that have been transferred so far during the
         *            download/upload, must not be negative.
         * @return This event builder for chaining, never {@code null}.
         */
        public Builder setTransferredBytes( long transferredBytes )
        {
            checkArgument( transferredBytes >= 0, "number of transferred bytes cannot be negative" );
            this.transferredBytes = transferredBytes;
            return this;
        }

        /**
         * Increments the total number of bytes that have been transferred so far during the download/upload.
         *
         * @param transferredBytes The number of bytes that have been transferred since the last event, must not be
         *            negative.
         * @return This event builder for chaining, never {@code null}.
         */
        public Builder addTransferredBytes( long transferredBytes )
        {
            checkArgument( transferredBytes >= 0, "number of transferred bytes cannot be negative" );
            this.transferredBytes += transferredBytes;
            return this;
        }

        /**
         * Sets the byte buffer holding the transferred bytes since the last event.
         *
         * @param buffer The byte buffer holding the transferred bytes since the last event, may be {@code null} if not
         *            applicable to the event.
         * @param offset The starting point of valid bytes in the array.
         * @param length The number of valid bytes, must not be negative.
         * @return This event builder for chaining, never {@code null}.
         */
        public Builder setDataBuffer( byte[] buffer, int offset, int length )
        {
            return setDataBuffer( ( buffer != null ) ? ByteBuffer.wrap( buffer, offset, length ) : null );
        }

        /**
         * Sets the byte buffer holding the transferred bytes since the last event.
         *
         * @param dataBuffer The byte buffer holding the transferred bytes since the last event, may be {@code null} if
         *            not applicable to the event.
         * @return This event builder for chaining, never {@code null}.
         */
        public Builder setDataBuffer( ByteBuffer dataBuffer )
        {
            this.dataBuffer = dataBuffer;
            return this;
        }

        /**
         * Sets the error that occurred during the transfer.
         *
         * @param exception The error that occurred during the transfer, may be {@code null} if none.
         * @return This event builder for chaining, never {@code null}.
         */
        public Builder setException( Exception exception )
        {
            this.exception = exception;
            return this;
        }

        /**
         * Builds a new transfer event from the current values of this builder. The state of the builder itself remains
         * unchanged.
         *
         * @return The transfer event, never {@code null}.
         */
        public TransferEvent build()
        {
            return new TransferEvent( requestType,
                                      session,
                                      resource,
                                      dataBuffer,
                                      transferredBytes,
                                      exception );
        }

    }

}
