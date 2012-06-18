package com.terradue.jcatalogue.transfer;

import java.io.File;

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
public interface TransferListener<T>
{

    void onError( Throwable t );

    void onError( String message );

    void onWarning( String message );

    void onFatal( String message );

    void onContentDownloadProgress( long current, long total );

    T onCompleted( File file );

}
