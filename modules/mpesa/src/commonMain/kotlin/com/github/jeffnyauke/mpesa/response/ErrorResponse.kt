/*
 * Copyright (c) 2024 Jeffrey Nyauke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jeffnyauke.mpesa.response

import kotlinx.serialization.Serializable

@Serializable
public open class ErrorResponse(
    internal open val errorCode: String,
    internal open val errorMessage: String,
) : Exception("$errorCode $errorMessage")

@Serializable
internal data class DetailedErrorResponse internal constructor(
    val requestId: String,
    val errorCode: String,
    val errorMessage: String,
) : Exception("$errorCode $errorMessage")
