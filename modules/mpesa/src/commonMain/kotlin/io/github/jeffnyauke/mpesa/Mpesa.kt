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

package io.github.jeffnyauke.mpesa

import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.ACCOUNT_BALANCE
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.B2C
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.C2B
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.C2B_REGISTER
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.DYNAMIC_QR
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.STK_PUSH
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.STK_PUSH_QUERY
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.TRANSACTION_REVERSAL
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.TRANSACTION_STATUS
import io.github.jeffnyauke.mpesa.client.HttpClientFactory
import io.github.jeffnyauke.mpesa.config.Environment
import io.github.jeffnyauke.mpesa.config.Environment.PRODUCTION
import io.github.jeffnyauke.mpesa.config.Environment.SANDBOX
import io.github.jeffnyauke.mpesa.requests.AccountBalanceRequest
import io.github.jeffnyauke.mpesa.requests.B2cRequest
import io.github.jeffnyauke.mpesa.requests.C2bRegisterRequest
import io.github.jeffnyauke.mpesa.requests.C2bRequest
import io.github.jeffnyauke.mpesa.requests.DynamicQrRequest
import io.github.jeffnyauke.mpesa.requests.StkPushQueryRequest
import io.github.jeffnyauke.mpesa.requests.StkPushRequest
import io.github.jeffnyauke.mpesa.requests.TransactionReversalRequest
import io.github.jeffnyauke.mpesa.requests.TransactionStatusRequest
import io.github.jeffnyauke.mpesa.responses.AccountBalanceResponse
import io.github.jeffnyauke.mpesa.responses.B2cResponse
import io.github.jeffnyauke.mpesa.responses.C2bRegisterResponse
import io.github.jeffnyauke.mpesa.responses.C2bResponse
import io.github.jeffnyauke.mpesa.responses.DynamicQrResponse
import io.github.jeffnyauke.mpesa.responses.StkPushQueryResponse
import io.github.jeffnyauke.mpesa.responses.StkPushResponse
import io.github.jeffnyauke.mpesa.responses.TransactionReversalResponse
import io.github.jeffnyauke.mpesa.responses.TransactionStatusResponse
import io.github.jeffnyauke.mpesa.store.BaseAuthStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/**
 * Provides various methods to interact with the Daraja v2.0 M-PESA API. For the official
 * documentation, see [Safaricom developer website](https://developer.safaricom.co.ke).
 *
 * @param consumerKey Daraja API consumer key.
 * @param consumerSecret Daraja API consumer secret.
 * @param environment The [Environment] of the API. Defaults to [Environment.PRODUCTION].
 * @param shouldEnableLogging Boolean value indicating whether to enable logging. Defaults to true
 *     if the environment is [Environment.SANDBOX], false otherwise.
 * @param httpClientEngine The HTTP client engine to use. Defaults to null, which uses the default
 *     engine for the current platform.
 * @param authStore A cache to store access tokens. Defaults to a new [BaseAuthStore] instance.
 */
public class Mpesa(
    private val consumerKey: String,
    private val consumerSecret: String,
    private val environment: Environment = PRODUCTION,
    private val shouldEnableLogging: Boolean = environment == SANDBOX,
    private val httpClientEngine: HttpClientEngine? = null,
    public val authStore: BaseAuthStore = BaseAuthStore(),
) {
    private val httpClient: HttpClient = HttpClientFactory().create(
        httpClientEngine,
        consumerKey,
        consumerSecret,
        environment,
        shouldEnableLogging,
        authStore
    )

    /**
     * Initiates an STK Push (Lipa na M-Pesa Online) transaction.
     *
     * STK Push - Lipa na M-Pesa Online API also known as M-PESA express (STK Push) is a
     * Merchant/Business initiated C2B (Customer to Business) Payment.
     *
     * Once you, our merchant integrate with the API, you will be able to send a payment prompt
     * on the customer's phone (Popularly known as STK Push Prompt) to your customer's M-PESA
     * registered phone number requesting them to enter their M-PESA pin to authorize and complete
     * payment.
     *
     * @param stkPushRequest The [StkPushRequest] object containing the request details.
     * @return The [StkPushResponse] object containing the acknowledgement details.
     * @throws ResponseException if there is an error while executing the API request.
     */
    public suspend fun initiateStkPush(stkPushRequest: StkPushRequest): StkPushResponse =
        executeApiRequest(STK_PUSH, stkPushRequest)

    /**
     * STK Push query - Check the status of a Lipa Na M-Pesa Online Payment.
     *
     * @param stkPushQueryRequest The [StkPushQueryRequest] request object.
     * @return The [StkPushQueryResponse] object containing the result of the query.
     * @throws ResponseException if there is an error while executing the API request.
     */
    public suspend fun queryStkPushStatus(stkPushQueryRequest: StkPushQueryRequest): StkPushQueryResponse =
        executeApiRequest(STK_PUSH_QUERY, stkPushQueryRequest)

    /**
     * C2B register - Register validation and confirmation URLs on M-Pesa
     *
     * Register URL API works in hand with Customer to Business (C2B) APIs and allows receiving
     * payment notifications to your paybill. This API enables you to register the callback URLs
     * via which you shall receive notifications for payments to your pay bill/till number.
     *
     * @param c2bRegisterRequest The [C2bRegisterRequest] request object containing the validation
     *     and confirmation URLs, and the short code of the paybill or till number.
     * @return A successful acknowledgement [C2bRegisterResponse] response object.
     * @throws ResponseException if there is an error while executing the API request.
     */
    public suspend fun registerC2bUrls(c2bRegisterRequest: C2bRegisterRequest): C2bRegisterResponse =
        executeApiRequest(C2B_REGISTER, c2bRegisterRequest)

    /**
     * Initiates a C2B transaction between a phone number and an M-Pesa short code registered on
     * M-Pesa.
     *
     * @param c2bRequest The [C2bRequest] object containing the transaction details.
     * @return A successful acknowledgement [C2bResponse] response object.
     * @throws ResponseException if there is an error while executing the API request.
     */
    public suspend fun initiateC2bTransaction(c2bRequest: C2bRequest): C2bResponse =
        executeApiRequest(C2B, c2bRequest)

    /**
     * B2C - Transact between an M-Pesa short code to a phone number registered on M-Pesa.
     *
     * @param b2cRequest The [B2cRequest] request object
     * @return successful acknowledgement [B2cResponse] response object.
     * @throws ResponseException if there is an error while executing the API request.
     */
    public suspend fun initiateB2cTransaction(b2cRequest: B2cRequest): B2cResponse =
        executeApiRequest(B2C, b2cRequest)

    /**
     * Transaction status - Check the status of a transaction.
     *
     * @param transactionStatusRequest The [TransactionStatusRequest] request object that contains
     *     the transactionId for which the status is to be retrieved.
     * @return successful acknowledgement [TransactionStatusResponse] response object.
     * @throws [ResponseException] if there is an error while executing the API request.
     */
    public suspend fun getTransactionStatus(
        transactionStatusRequest: TransactionStatusRequest
    ): TransactionStatusResponse = executeApiRequest(TRANSACTION_STATUS, transactionStatusRequest)

    /**
     * Account balance - Enquire the balance on an M-Pesa BuyGoods (Till Number).
     *
     * The Account Balance API is used to request the account balance of a short code. This can be
     * used for both B2C, buy goods and pay bill accounts.
     *
     * @param accountBalanceRequest The [AccountBalanceRequest] request object.
     * @return A successful acknowledgement [AccountBalanceResponse] response object.
     * @throws ResponseException if there is an error while executing the API request.
     */
    public suspend fun getAccountBalance(accountBalanceRequest: AccountBalanceRequest): AccountBalanceResponse =
        executeApiRequest(ACCOUNT_BALANCE, accountBalanceRequest)

    /**
     * Reversal - Reverses a C2B M-Pesa transaction.
     *
     * Once a customer pays and there is a need to reverse the transaction, the organization will
     * use this API to reverse the amount.
     *
     * @param transactionReversalRequest The [TransactionReversalRequest] request object containing
     *     the transaction details for reversal.
     * @return A successful acknowledgement [TransactionReversalResponse] response object.
     * @throws ResponseException if there is an error while executing the API request.
     */
    public suspend fun transactionReversal(
        transactionReversalRequest: TransactionReversalRequest
    ): TransactionReversalResponse =
        executeApiRequest(TRANSACTION_REVERSAL, transactionReversalRequest)

    /**
     * Dynamic QR - Generates a dynamic M-PESA QR Code.
     *
     * Use this API to generate a Dynamic QR which enables Safaricom M-PESA customers who have My
     * Safaricom App or M-PESA app, to scan a QR (Quick Response) code, to capture till number and
     * amount then authorize to pay for goods and services at select LIPA NA M-PESA (LNM) merchant
     * outlets.
     *
     * @param dynamicQrRequest The [DynamicQrRequest] request object.
     * @return A successful [DynamicQrResponse] response object containing the QR code
     * @throws ResponseException if there is an error while executing the API request.
     */
    public suspend fun generateDynamicQr(dynamicQrRequest: DynamicQrRequest): DynamicQrResponse =
        executeApiRequest(DYNAMIC_QR, dynamicQrRequest)

    private suspend inline fun <reified T : Any> executeApiRequest(
        apiEndpoint: String,
        requestBody: Any
    ): T = httpClient.post(apiEndpoint) { setBody(requestBody) }.body<T>()
}
