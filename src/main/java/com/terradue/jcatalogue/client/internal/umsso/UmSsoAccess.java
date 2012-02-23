package com.terradue.jcatalogue.client.internal.umsso;

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

import java.net.URI;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.terradue.jcatalogue.client.HttpMethod;
import com.terradue.jcatalogue.client.Parameter;

/**
 * @since 0.8
 */
@RequiredArgsConstructor
@Getter
@ToString
public final class UmSsoAccess
{

    private final URI loginFormUrl;

    private final HttpMethod httpMethod;

    private final Parameter[] parameters;

}
