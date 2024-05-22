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

package com.github.jeffnyauke.mpesa.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
internal fun getDarajaTimestamp(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.of("GMT+03:00")
): String {
    return clock.now().toLocalDateTime(timeZone).toDarajaTimestamp()
}

internal fun LocalDateTime.toDarajaTimestamp(): String {
    return with(this) {
        buildString {
            append(year)
            append(monthNumber.addLeadingZero())
            append(dayOfMonth.addLeadingZero())
            append(hour.addLeadingZero())
            append(minute.addLeadingZero())
            append(second.addLeadingZero())
        }
    }
}

private const val MAX_LENGTH_OF_DATE_COMPONENT = 10
private const val ZERO_PREFIX = "0"

internal fun Int.addLeadingZero(): String {
    return if (this < MAX_LENGTH_OF_DATE_COMPONENT) {
        ZERO_PREFIX + this
    } else {
        this.toString()
    }
}
