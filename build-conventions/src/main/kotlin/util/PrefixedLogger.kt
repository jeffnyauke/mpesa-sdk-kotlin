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

package util

import org.gradle.api.tasks.Internal
import org.slf4j.Logger
import org.slf4j.Marker
import org.slf4j.MarkerFactory

/** Standardised logging utilities with the plugin name prefix. */
interface PrefixedLogger {
    @get:Internal
    val prefix: String

    @get:Internal
    val marker: Marker get() = MarkerFactory.getMarker(prefix)

    /**
     * Logger provider
     *
     * @return logger to use for all logging events
     */
    @Internal
    fun getLogger(): Logger

    /**
     * Logs at error level
     *
     * @param message provider
     */
    fun error(message: () -> String) {
        if (getLogger().isErrorEnabled) {
            getLogger().error(marker, "[$prefix] ${message()}")
        }
    }

    /**
     * Logs at warn level
     *
     * @param message provider
     */
    fun warn(message: () -> String) {
        if (getLogger().isWarnEnabled) {
            getLogger().warn(marker, "[$prefix] ${message()}")
        }
    }

    /**
     * Logs at info level
     *
     * @param message provider
     */
    fun info(message: () -> String) {
        if (getLogger().isInfoEnabled) {
            getLogger().info(marker, "[$prefix] ${message()}")
        }
    }

    /**
     * Logs at debug level
     *
     * @param message provider
     */
    fun debug(message: () -> String) {
        if (getLogger().isDebugEnabled) {
            getLogger().debug(marker, "[$prefix] ${message()}")
        }
    }
}
