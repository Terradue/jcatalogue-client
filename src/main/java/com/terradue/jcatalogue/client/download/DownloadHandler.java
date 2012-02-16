package com.terradue.jcatalogue.client.download;

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

import java.io.File;

public interface DownloadHandler
{

    void onError( Throwable t );

    void onError( String message );

    void onWarning( String message );

    void onFatal( String message );

    @Deprecated
    void onSuccess( File file );

    /**
     * @since 0.8
     */
    void onCompleted( File file );

}
