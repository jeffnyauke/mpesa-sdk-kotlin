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

import com.github.jeffnyauke.mpesa.constants.Network.CommandId.ACCOUNT_BALANCE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Account balance request.
 *
 * @param initiator This is the credential/username used to authenticate the transaction request.
 *     Sample: Testapi772.
 * @param securityCredential Base64 encoded string of the M-PESA short code and password, which is
 *     encrypted using M-PESA public key and validates the transaction on M-PESA Core system. It
 *     indicates the Encrypted credential of the initiator getting the account balance. Its value
 *     must match the inputted value of the parameter IdentifierType.
 * @param commandId A unique command is passed to the M-PESA system. Max length is 64. Sample:
 *     AccountBalance
 * @param partyA The shortcode of the organization querying for the account balance. Sample: 60072
 * @param identifierType Type of organization querying for the account balance.
 * @param resultUrl It indicates the destination URL which Daraja should send the result message to.
 * @param queueTimeOutUrl The end-point that receives a timeout message.
 * @param remarks Comments that are sent along with the transaction. Sample: tests
 */
@Serializable
public data class AccountBalanceRequest(
    @SerialName("Initiator") val initiator: String,
    @SerialName("SecurityCredential") val securityCredential: String,
    @SerialName("CommandID") val commandId: String,
    @SerialName("PartyA") val partyA: String,
    @SerialName("IdentifierType") val identifierType: String,
    @SerialName("ResultURL") val resultUrl: String,
    @SerialName("QueueTimeOutURL") val queueTimeOutUrl: String,
    @SerialName("Remarks") val remarks: String,
) {
    /**
     * @param initiator This is the credential/username used to authenticate the transaction
     *     request. Sample: Testapi772.
     * @param securityCredential Base64 encoded string of the M-PESA short code and password, which
     *     is encrypted using M-PESA public key and validates the transaction on M-PESA Core system.
     *     It indicates the Encrypted credential of the initiator getting the account balance. Its
     *     value must match the inputted value of the parameter IdentifierType.
     * @param partyA The shortcode of the organization querying for the account balance. Sample:
     *     60072
     * @param accountBalanceOrganizationType Type of organization querying for the account balance.
     *     Sample: [AccountBalanceOrganizationType.Shortcode] or
     *     [AccountBalanceOrganizationType.TillNumber]
     * @param resultUrl It indicates the destination URL which Daraja should send the result message
     *     to.
     * @param queueTimeOutUrl The end-point that receives a timeout message.
     * @param remarks Comments that are sent along with the transaction. Sample: tests
     */
    public constructor(
        initiator: String,
        securityCredential: String,
        partyA: String,
        accountBalanceOrganizationType: AccountBalanceOrganizationType,
        resultUrl: String,
        queueTimeOutUrl: String,
        remarks: String,
    ) : this(
        initiator,
        securityCredential,
        ACCOUNT_BALANCE,
        partyA,
        accountBalanceOrganizationType.identifier,
        resultUrl,
        queueTimeOutUrl,
        remarks
    )
}
