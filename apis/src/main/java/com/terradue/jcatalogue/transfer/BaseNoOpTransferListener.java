package com.terradue.jcatalogue.transfer;

/**
 * A skeleton implementation for custom transfer listeners. The callback methods in this class do nothing.
 */
public abstract class BaseNoOpTransferListener
    implements TransferListener
{

    public void transferInitiated( TransferEvent event )
        throws TransferCancelledException
    {
    }

    public void transferStarted( TransferEvent event )
        throws TransferCancelledException
    {
    }

    public void transferProgressed( TransferEvent event )
        throws TransferCancelledException
    {
    }

    public void transferCorrupted( TransferEvent event )
        throws TransferCancelledException
    {
    }

    public void transferSucceeded( TransferEvent event )
    {
    }

    public void transferFailed( TransferEvent event )
    {
    }

}
