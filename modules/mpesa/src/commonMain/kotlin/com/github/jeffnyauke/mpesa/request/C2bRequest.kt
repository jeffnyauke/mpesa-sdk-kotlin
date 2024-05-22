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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * C2b request
 *
 * @param shortCode Usually, a unique number is tagged to an M-PESA pay bill/till number of the
 *     organization.
 * @param commandId This is a unique command that specifies B2C transaction type.
 * @param amount The amount of money being sent to the short code.
 * @param msisdn This is the mobile number of the customer making the payment. Sample: MSISDN (12
 *     digits Mobile Number) e.g. 254XXXXXXXXX.
 * @param billRefNumber This is the account number for which the customer is making the payment.
 *     This is only applicable to Customer PayBill Transactions. Sample: An alphanumeric value of up
 *     to 20 characters.
 */
@Serializable
public data class C2bRequest(
    @SerialName("ShortCode") val shortCode: String,
    @SerialName("CommandID") val commandId: String,
    @SerialName("Amount") val amount: String,
    @SerialName("Msisdn") val msisdn: String,
    @SerialName("BillRefNumber") val billRefNumber: String,
) {
    /**
     * @param shortCode Usually, a unique number is tagged to an M-PESA pay bill/till number of the
     *     organization.
     * @param c2bCommandId This is a unique command that specifies B2C transaction type. Sample:
     *     [C2bCommandId.CustomerPayBillOnline ].
     * @param amount The amount of money being sent to the short code.
     * @param msisdn This is the mobile number of the customer making the payment. Sample: MSISDN
     *     (12 digits Mobile Number) e.g. 254XXXXXXXXX.
     * @param billRefNumber This is the account number for which the customer is making the payment.
     *     This is only applicable to Customer PayBill Transactions. Sample: An alphanumeric value
     *     of up to 20 characters.
     */
    public constructor(
        shortCode: String,
        c2bCommandId: C2bCommandId,
        amount: String,
        msisdn: String,
        billRefNumber: String,
    ) : this(shortCode, c2bCommandId.name, amount, msisdn, billRefNumber)
}
