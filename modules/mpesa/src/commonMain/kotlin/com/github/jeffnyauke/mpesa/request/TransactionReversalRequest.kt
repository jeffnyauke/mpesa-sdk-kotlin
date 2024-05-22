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

import com.github.jeffnyauke.mpesa.constants.Network.CommandId.TRANSACTION_REVERSAL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Transaction reversal request.
 *
 * @param initiator The name of the initiator to initiate the request.
 * @param securityCredential Encrypted Credential of the user getting transaction amount.
 * @param commandId Takes only the 'TransactionReversal' Command id.
 * @param transactionId This is the Mpesa Transaction ID of the transaction which you wish to
 *     reverse.
 * @param amount The amount transacted in the transaction is to be reversed, down to the cent.
 * @param receiverParty The organization that receives the transaction. Sample: Shortcode (6-9
 *     digits).
 * @param receiverIdentifierType Type of organization receiving the transaction.
 * @param resultUrl The path that stores information about the transaction.
 * @param queueTimeOutUrl The path that stores information of the time-out transaction.
 * @param remarks Comments that are sent along with the transaction.
 * @param occasion Optional Parameter. Sample: A sequence of characters up to 100.
 */
@Serializable
public data class TransactionReversalRequest(
    @SerialName("Initiator") val initiator: String,
    @SerialName("SecurityCredential") val securityCredential: String,
    @SerialName("CommandID") val commandId: String,
    @SerialName("TransactionID") val transactionId: String,
    @SerialName("Amount") val amount: String,
    @SerialName("ReceiverParty") val receiverParty: String,
    @SerialName("ReceiverIdentifierType") val receiverIdentifierType: String,
    @SerialName("ResultURL") val resultUrl: String,
    @SerialName("QueueTimeOutURL") val queueTimeOutUrl: String,
    @SerialName("Remarks") val remarks: String,
    @SerialName("Occasion") val occasion: String,
) {
    /**
     * @param initiator The name of the initiator to initiate the request.
     * @param securityCredential Encrypted Credential of the user getting transaction amount.
     * @param transactionId This is the Mpesa Transaction ID of the transaction which you wish to
     *     reverse.
     * @param amount The amount transacted in the transaction is to be reversed, down to the cent.
     * @param receiverParty The organization that receives the transaction. Sample: Shortcode (6-9
     *     digits).
     * @param transactionReversalReceiverOrganizationType Type of organization receiving the
     *     transaction. Sample: [TransactionReversalReceiverOrganizationType.Default].
     * @param resultUrl The path that stores information about the transaction.
     * @param queueTimeOutUrl The path that stores information of the time-out transaction.
     * @param remarks Comments that are sent along with the transaction.
     * @param occasion Optional Parameter. Sample: A sequence of characters up to 100.
     */
    public constructor(
        initiator: String,
        securityCredential: String,
        transactionId: String,
        amount: String,
        receiverParty: String,
        transactionReversalReceiverOrganizationType: TransactionReversalReceiverOrganizationType,
        resultUrl: String,
        queueTimeOutUrl: String,
        remarks: String,
        occasion: String,
    ) : this(
        initiator,
        securityCredential,
        TRANSACTION_REVERSAL,
        transactionId,
        amount,
        receiverParty,
        transactionReversalReceiverOrganizationType.identifier,
        resultUrl,
        queueTimeOutUrl,
        remarks,
        occasion,
    )
}
