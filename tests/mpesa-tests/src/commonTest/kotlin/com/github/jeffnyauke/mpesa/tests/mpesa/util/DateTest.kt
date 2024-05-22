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

package com.github.jeffnyauke.mpesa.tests.mpesa.util

/*import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DateTest {
  @Test
  fun `getDarajaTimestamp_should_work_correctly`() {
    val clock = Clock.fixed(Instant.DISTANT_FUTURE)    // Fixed clock instance
    val timeZone = TimeZone.of("GMT+03:00")             // The same timezone as in your function

    val timestamp = getDarajaTimestamp(clock, timeZone)

    // The fixed clock set to the distant future should give a long timestamp string
    assertTrue(timestamp.length > 5, "Timestamp should be longer than 5 characters")
  }

  @Test
  fun `toDarajaTimestamp_should_work_correctly`() {
    val localDateTime = LocalDateTime(2023, 12, 30, 15, 45, 12)    // An example date

    val timestamp = localDateTime.toDarajaTimestamp()

    // The given date should be formatted as: "20231230154512" (yyyyMMddHHmmss)
    assertEquals("20231230154512", timestamp)
  }

  @Test
  fun `addLeadingZero_should_add_lead_zero_for_numbers_less_than_10`() {
    assertEquals("01", 1.addLeadingZero())
    assertEquals("09", 9.addLeadingZero())
  }

  @Test
  fun `addLeadingZero_should_not_add_lead_zero_for_numbers_equal_or_greater_than_10`() {
    assertEquals("10", 10.addLeadingZero())
    assertEquals("99", 99.addLeadingZero())
  }

  public class FixedClock(private val fixedInstant: Instant): Clock {
    override fun now(): Instant = fixedInstant
  }

  private fun Clock.Companion.fixed(fixedInstant: Instant): Clock = FixedClock(fixedInstant)
}*/
