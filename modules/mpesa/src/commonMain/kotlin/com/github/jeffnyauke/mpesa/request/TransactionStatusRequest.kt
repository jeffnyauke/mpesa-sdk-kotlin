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

import com.github.jeffnyauke.mpesa.constants.Network.CommandId.TRANSACTION_STATUS_QUERY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Transaction status request.
 *
 * @param initiator The name of the initiator initiating the request. Sample: This is the
 *     credential/username used to authenticate the transaction request.
 * @param securityCredential Encrypted credential of the user getting transaction status. Sample:
 *     Encrypted password for the initiator to authenticate the transaction request.
 * @param commandId Takes only the 'TransactionStatusQuery' Command ID.
 * @param transactionId Unique identifier to identify a transaction on Mpesa. Sample: LXXXXXX1234.
 * @param originatorConversationId This is a global unique identifier for the transaction request
 *     returned by the API proxy upon successful request submission. If you don’t have the M-PESA
 *     transaction ID you can use this to query. Sample: AG_20190826_0000777ab7d848b9e721.
 * @param partyA Organization/MSISDN receiving the transaction. Sample: Shortcode (6-9 digits)
 *     MSISDN (12 Digits).
 * @param identifierType Type of organization receiving the transaction. Sample:
 *     [TransactionStatusOrganizationType.BuyGoods].
 * @param resultUrl The path that stores information of a transaction. Sample: https://ip:port/path
 *     or domain:port/path.
 * @param queueTimeOutUrl The path that stores information of timeout transaction. Sample:
 *     https://ip:port/path or domain:port/path.
 * @param remarks Comments that are sent along with the transaction. Sample: A sequence of
 *     characters up to 100.
 * @param occasion Optional parameter. Sample: A sequence of characters up to 100.
 */
@Serializable
public data class TransactionStatusRequest(
    @SerialName("Initiator") val initiator: String,
    @SerialName("SecurityCredential") val securityCredential: String,
    @SerialName("CommandID") val commandId: String,
    @SerialName("TransactionID") val transactionId: String,
    @SerialName("OriginatorConversationId") val originatorConversationId: String,
    @SerialName("PartyA") val partyA: String,
    @SerialName("IdentifierType") val identifierType: String,
    @SerialName("ResultURL") val resultUrl: String,
    @SerialName("QueueTimeOutURL") val queueTimeOutUrl: String,
    @SerialName("Remarks") val remarks: String,
    @SerialName("Occasion") val occasion: String
) {
    /**
     * @param initiator The name of the initiator initiating the request. Sample: This is the
     *     credential/username used to authenticate the transaction request.
     * @param securityCredential Encrypted credential of the user getting transaction status.
     *     Sample: Encrypted password for the initiator to authenticate the transaction request.
     * @param transactionId Unique identifier to identify a transaction on Mpesa. Sample:
     *     LXXXXXX1234.
     * @param originatorConversationId This is a global unique identifier for the transaction
     *     request returned by the API proxy upon successful request submission. If you don’t have
     *     the M-PESA transaction ID you can use this to query. Sample:
     *     AG_20190826_0000777ab7d848b9e721.
     * @param partyA Organization/MSISDN receiving the transaction. Sample: Shortcode (6-9 digits)
     *     MSISDN (12 Digits).
     * @param transactionStatusOrganizationType Type of organization receiving the transaction.
     *     Sample: [TransactionStatusOrganizationType.BuyGoods].
     * @param resultUrl The path that stores information of a transaction. Sample:
     *     https://ip:port/path or domain:port/path.
     * @param queueTimeOutUrl The path that stores information of timeout transaction. Sample:
     *     https://ip:port/path or domain:port/path.
     * @param remarks Comments that are sent along with the transaction. Sample: A sequence of
     *     characters up to 100.
     * @param occasion Optional parameter. Sample: A sequence of characters up to 100.
     */
    public constructor(
        initiator: String,
        securityCredential: String,
        transactionId: String,
        originatorConversationId: String,
        partyA: String,
        transactionStatusOrganizationType: TransactionStatusOrganizationType,
        resultUrl: String,
        queueTimeOutUrl: String,
        remarks: String,
        occasion: String,
    ) : this(
        initiator,
        securityCredential,
        TRANSACTION_STATUS_QUERY,
        transactionId,
        originatorConversationId,
        partyA,
        transactionStatusOrganizationType.identifier,
        resultUrl,
        queueTimeOutUrl,
        remarks,
        occasion
    )
}
