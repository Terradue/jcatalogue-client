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

/**
 * A listener being notified of artifact/metadata transfers from/to remote repositories. The listener may be called from
 * an arbitrary thread. Reusing common regular expression syntax, the sequence of events is roughly as follows:
 *
 * <pre>
 * INITIATED ( STARTED PROGRESSED* CORRUPTED? )* ( SUCCEEDED | FAILED )
 * </pre>
 *
 * <em>Note:</em> Implementors are strongly advised to inherit from {@link BaseNoOpTransferListener} instead of directly
 * implementing this interface.
 *
 * @see org.eclipse.aether.RepositoryListener
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface TransferListener
{

    /**
     * Notifies the listener about the initiation of a transfer. This event gets fired before any actual network access
     * to the remote repository.
     *
     * @param event The event details, must not be {@code null}.
     * @throws TransferCancelledException If the transfer should be aborted.
     */
    void transferInitiated( TransferEvent event )
        throws TransferCancelledException;

    /**
     * Notifies the listener about the start of a data transfer, i.e. the successful connection to the remote
     * repository.
     *
     * @param event The event details, must not be {@code null}.
     * @throws TransferCancelledException If the transfer should be aborted.
     */
    void transferStarted( TransferEvent event )
        throws TransferCancelledException;

    /**
     * Notifies the listener about some progress in the data transfer. This event may even be fired if actually zero
     * bytes have been transferred since the last event, for instance to enable cancellation.
     *
     * @param event The event details, must not be {@code null}.
     * @throws TransferCancelledException If the transfer should be aborted.
     */
    void transferProgressed( TransferEvent event )
        throws TransferCancelledException;

    /**
     * Notifies the listener that a checksum validation failed. {@link TransferEvent#getException()} will be of type
     * {@link ChecksumFailureException} and can be used to query further details about the expected/actual checksums.
     *
     * @param event The event details, must not be {@code null}.
     * @throws TransferCancelledException If the transfer should be aborted.
     */
    void transferCorrupted( TransferEvent event )
        throws TransferCancelledException;

    /**
     * Notifies the listener about the successful completion of a transfer.
     *
     * @param event The event details, must not be {@code null}.
     */
    void transferSucceeded( TransferEvent event );

    /**
     * Notifies the listener about the unsuccessful termination of a transfer. {@link TransferEvent#getException()} will
     * provide further information about the failure.
     *
     * @param event The event details, must not be {@code null}.
     */
    void transferFailed( TransferEvent event );

}
