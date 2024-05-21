/*
 * Copyright 2023 Jeffrey Nyauke
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

package com.github.jeffnyauke.mpesa.exception

import com.github.jeffnyauke.mpesa.Mpesa
import com.github.jeffnyauke.mpesa.models.response.ErrorResponse

/**
 * Root exception for the [Mpesa] client and the <code>mpesa-sdk-kotlin</code> library.
 */
public class DarajaException : Exception {
    internal var error: ErrorResponse? = null
        private set

    internal constructor(error: ErrorResponse?) {
        this.error = error
    }

    internal constructor(cause: Throwable?) : super(cause)
    internal constructor(message: String?) : super(message)
    internal constructor(message: String?, cause: Throwable?) : super(message, cause)

    override val message: String
        get() = error?.toString() ?: super.message ?: "<no message>"
}
