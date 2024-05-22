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

package com.github.jeffnyauke.mpesa.request

import com.github.jeffnyauke.mpesa.util.encodeBase64
import com.github.jeffnyauke.mpesa.util.getDarajaTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stk push query request
 *
 * @param businessShortCode This is organizations shortcode (PayBill or BuyGoods - A 5 to 7-digit
 *     account number) used to identify an organization and receive the transaction. Sample:
 *     Shortcode (5 to 7 digits) e.g. 654321.
 * @param password This is the password used for encrypting the request sent: a base64 encoded
 *     string. (The base64 string is a combination of Shortcode+Passkey+Timestamp).
 * @param timestamp This is the Timestamp of the transaction, normally in the format of
 *     YEAR+MONTH+DATE+HOUR+MINUTE+SECOND (YYYYMMDDHHMMSS). Each part should be at least two digits,
 *     apart from the year which takes four digits.
 * @param checkoutRequestId This is a global unique identifier of the processed checkout transaction
 *     request. Sample: ws_CO_DMZ_123212312_2342347678234.
 */
@Serializable
public data class StkPushQueryRequest(
    @SerialName("BusinessShortCode") val businessShortCode: String,
    @SerialName("Password") val password: String,
    @SerialName("Timestamp") val timestamp: String,
    @SerialName("CheckoutRequestID") val checkoutRequestId: String,
) {
    /**
     * @param businessShortCode This is organizations shortcode (PayBill or BuyGoods - A 5 to
     *     7-digit account number) used to identify an organization and receive the transaction.
     *     Sample: Shortcode (5 to 7 digits) e.g. 654321
     * @param passKey If you need a passkey for mpesa online payments solutions (specifically
     *     express STK push) simply email m-pesabusiness@safaricom.co.ke and they will send it to
     *     you.
     * @param checkoutRequestId This is a global unique identifier of the processed checkout
     *     transaction request. Sample: ws_CO_DMZ_123212312_2342347678234
     */
    public constructor(
        businessShortCode: String,
        passKey: String,
        checkoutRequestId: String,
    ) : this(
        businessShortCode,
        "$businessShortCode$passKey${getDarajaTimestamp()}".encodeBase64(),
        getDarajaTimestamp(),
        checkoutRequestId
    )
}
