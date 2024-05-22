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
 * B2c request
 *
 * @param initiatorName This is an API user created by the Business Administrator of the M-PESA Bulk
 *     disbursement account that is active and authorized to initiate B2C transactions via API.
 *     Sample: initiator_1, John_Doe,John Doe
 * @param securityCredential This is the value obtained after encrypting the API initiator password.
 *     The password on Sandbox has been provisioned on the simulator. However, on production the
 *     password is created when the user is being created on the M-PESA organization portal.
 * @param commandId This is a unique command that specifies B2C transaction type.
 * @param amount The amount of money being sent to the customer.
 * @param partyA This is the B2C organization shortcode from which the money is to be sent.
 * @param partyB This is the customer mobile number to receive the amount. The number should have
 *     the country code (254) without the plus sign.
 * @param resultUrl This is the URL to be specified in your request that will be used by M-Pesa to
 *     send notification upon processing of the payment request.
 * @param queueTimeOutUrl This is the URL to be specified in your request that will be used by API
 *     Proxy to send notification in case the payment request is timed out while awaiting processing
 *     in the queue.
 * @param remarks Any additional information to be associated with the transaction.
 * @param occasion Any additional information to be associated with the transaction.
 */
@Serializable
public data class B2cRequest(
    @SerialName("InitiatorName") val initiatorName: String,
    @SerialName("SecurityCredential") val securityCredential: String,
    @SerialName("CommandID") val commandId: String,
    @SerialName("Amount") val amount: Float,
    @SerialName("PartyA") val partyA: String,
    @SerialName("PartyB") val partyB: String,
    @SerialName("ResultURL") val resultUrl: String,
    @SerialName("QueueTimeOutURL") val queueTimeOutUrl: String,
    @SerialName("Remarks") val remarks: String,
    @SerialName("Occasion") val occasion: String,
) {
    /**
     * @param initiatorName This is an API user created by the Business Administrator of the M-PESA
     *     Bulk disbursement account that is active and authorized to initiate B2C transactions via
     *     API. Sample: initiator_1, John_Doe,John Doe
     * @param securityCredential This is the value obtained after encrypting the API initiator
     *     password. The password on Sandbox has been provisioned on the simulator. However, on
     *     production the password is created when the user is being created on the M-PESA
     *     organization portal.
     * @param b2cCommandId This is a unique command that specifies B2C transaction type. Sample:
     *     [B2cCommandId.SalaryPayment]
     * @param amount The amount of money being sent to the customer.
     * @param partyA This is the B2C organization shortcode from which the money is to be sent.
     * @param partyB This is the customer mobile number to receive the amount. The number should
     *     have the country code (254) without the plus sign.
     * @param resultUrl This is the URL to be specified in your request that will be used by M-Pesa
     *     to send notification upon processing of the payment request.
     * @param queueTimeOutUrl This is the URL to be specified in your request that will be used by
     *     API Proxy to send notification in case the payment request is timed out while awaiting
     *     processing in the queue.
     * @param remarks Any additional information to be associated with the transaction.
     * @param occasion Any additional information to be associated with the transaction.
     */
    public constructor(
        initiatorName: String,
        securityCredential: String,
        b2cCommandId: B2cCommandId,
        amount: String,
        partyA: String,
        partyB: String,
        resultUrl: String,
        queueTimeOutUrl: String,
        remarks: String,
        occasion: String
    ) : this(
        initiatorName,
        securityCredential,
        b2cCommandId.name,
        amount.toFloat(),
        partyA,
        partyB,
        resultUrl,
        queueTimeOutUrl,
        remarks,
        occasion
    )
}
