/*
 * Copyright 2023 Jeffrey Nyauke
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

package com.github.jeffnyauke.mpesa

import com.github.jeffnyauke.mpesa.config.Environment
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.ACCOUNT_BALANCE
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.B2C
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.C2B
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.C2B_REGISTER
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.DYNAMIC_QR
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.STK_PUSH
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.STK_PUSH_QUERY
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.TRANSACTION_REVERSAL
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.TRANSACTION_STATUS
import com.github.jeffnyauke.mpesa.models.AccessToken
import com.github.jeffnyauke.mpesa.models.request.AccountBalanceRequest
import com.github.jeffnyauke.mpesa.models.request.B2cRequest
import com.github.jeffnyauke.mpesa.models.request.C2bRegisterRequest
import com.github.jeffnyauke.mpesa.models.request.C2bRequest
import com.github.jeffnyauke.mpesa.models.request.DynamicQrRequest
import com.github.jeffnyauke.mpesa.models.request.StkPushQueryRequest
import com.github.jeffnyauke.mpesa.models.request.StkPushRequest
import com.github.jeffnyauke.mpesa.models.request.TransactionReversalRequest
import com.github.jeffnyauke.mpesa.models.request.TransactionStatusRequest
import com.github.jeffnyauke.mpesa.models.response.AccountBalanceResponse
import com.github.jeffnyauke.mpesa.models.response.B2cResponse
import com.github.jeffnyauke.mpesa.models.response.C2bRegisterResponse
import com.github.jeffnyauke.mpesa.models.response.C2bResponse
import com.github.jeffnyauke.mpesa.models.response.DynamicQrResponse
import com.github.jeffnyauke.mpesa.models.response.StkPushQueryResponse
import com.github.jeffnyauke.mpesa.models.response.StkPushResponse
import com.github.jeffnyauke.mpesa.models.response.TransactionReversalResponse
import com.github.jeffnyauke.mpesa.models.response.TransactionStatusResponse
import com.github.jeffnyauke.mpesa.network.bodyOrThrow
import com.github.jeffnyauke.mpesa.network.createHttpClient
import com.github.jeffnyauke.mpesa.network.commonKtorConfiguration
import io.github.reactivecircus.cache4k.Cache
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/**
 * Provides various methods to interact with the Daraja v2.0 M-PESA API.
 * For the official documentation, see [Safaricom developer website](https://developer.safaricom.co.ke).
 *
 * @param consumerKey Daraja API consumer key.
 * @param consumerSecret Daraja API consumer secret.
 * @param environment The [Environment] of the API. Defaults to [Environment.SANDBOX].
 * @param shouldEnableLogging Boolean value indicating whether to enable logging. Defaults to true
 *     if the environment is [Environment.SANDBOX], false otherwise.
 * @param httpClientEngine The HTTP client engine to use. Defaults to null, which uses the default
 *     engine for the current platform.
 * @param cache A cache to store access tokens. Defaults to a new [Cache] instance.
 * @param httpClientConfig A lambda expression that configures the HTTP client. Defaults to a
 *     configuration that uses the provided consumer key, consumer secret, environment, and logging
 *     settings.
 * @param httpClient The HTTP client to use. Defaults to a new client configured with the provided
 *     configuration.
 */
public class Mpesa(
    private val consumerKey: String,
    private val consumerSecret: String,
    private val environment: Environment = Environment.SANDBOX,
    private val shouldEnableLogging: Boolean = environment == Environment.SANDBOX,
    private val httpClientEngine: HttpClientEngine? = null,
    public val cache: Cache<Long, AccessToken> = Cache.Builder<Long, AccessToken>().build(),
    private val httpClientConfig: HttpClientConfig<*>.() -> Unit = {
        commonKtorConfiguration(
            consumerKey, consumerSecret, environment, shouldEnableLogging, cache
        ).invoke(this)
    },
    private val httpClient: HttpClient = createHttpClient(httpClientEngine, httpClientConfig)
) {
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
     * @return A [StkPushResponse] object containing the acknowledgement details.
     * @throws DarajaException if there is an error with the API call.
     */
    public suspend fun initiateStkPush(stkPushRequest: StkPushRequest): StkPushResponse =
        executeApiRequest(STK_PUSH, stkPushRequest)

    /**
     * STK Push query - Check the status of a Lipa Na M-Pesa Online Payment.
     *
     * @param stkPushQueryRequest The [StkPushQueryRequest] request object.
     * @return The [StkPushQueryResponse] object containing the result of the query.
     * @throws Exception If the API call fails.
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
     * @param c2bRegisterRequest The request object containing the validation and confirmation URLs,
     * and the short code of the paybill or till number.
     *
     * @return A successful acknowledgement [C2bRegisterResponse] response object.
     *
     * @throws DarajaException if there is an error while executing the API request.
     *
     * @see C2bRegisterRequest
     * @see C2bRegisterResponse
     */
    public suspend fun registerC2bUrls(c2bRegisterRequest: C2bRegisterRequest): C2bRegisterResponse =
        executeApiRequest(C2B_REGISTER, c2bRegisterRequest)

    /**
     * Initiates a C2B transaction between a phone number and an M-Pesa short code registered on M-Pesa.
     *
     * @param c2bRequest The [C2bRequest] object containing the transaction details.
     * @return A [C2bResponse] object representing the transaction acknowledgement.
     *
     * @throws DarajaException if the request fails.
     */
    public suspend fun initiateC2bTransaction(c2bRequest: C2bRequest): C2bResponse =
        executeApiRequest(C2B, c2bRequest)

    /**
     * B2C - Transact between an M-Pesa short code to a phone number registered on M-Pesa.
     *
     * @param b2cRequest The B2C request object
     *
     * @return successful acknowledgement [B2cResponse] response object.
     *
     * @throws DarajaApiException if there is an error in the API call.
     */
    public suspend fun initiateB2cTransaction(b2cRequest: B2cRequest): B2cResponse =
        executeApiRequest(B2C, b2cRequest)

    /**
     * Transaction status - Check the status of a transaction.
     *
     * @param transactionStatusRequest Request object that contains the transactionId for which the status is to be retrieved.
     * @return successful acknowledgement [TransactionStatusResponse] response object.
     * @throws [TransactionStatusException] if the transaction status request failed.
     * @throws [IllegalArgumentException] if the transaction status request is null.
     * @see [TransactionStatusRequest]
     * @see [TransactionStatusResponse]
     */
    public suspend fun getTransactionStatus(transactionStatusRequest: TransactionStatusRequest): TransactionStatusResponse =
        executeApiRequest(TRANSACTION_STATUS, transactionStatusRequest)

    /**
     * Account balance - Enquire the balance on an M-Pesa BuyGoods (Till Number).
     *
     * The Account Balance API is used to request the account balance of a short code. This can be
     * used for both B2C, buy goods and pay bill accounts.
     *
     * @param accountBalanceRequest Account balance request object.
     *
     * @return successful acknowledgement [AccountBalanceResponse] response object.
     *
     * @throws MpesaException when there is an error in the request.
     */
    public suspend fun getAccountBalance(accountBalanceRequest: AccountBalanceRequest): AccountBalanceResponse =
        executeApiRequest(ACCOUNT_BALANCE, accountBalanceRequest)

    /**
     * Reversal - Reverses a C2B M-Pesa transaction.
     *
     * Once a customer pays and there is a need to reverse the transaction, the organization will
     * use this API to reverse the amount.
     *
     * @param transactionReversalRequest The request object containing the transaction details for reversal.
     *
     * @return A [TransactionReversalResponse] object containing the reversal details.
     *
     * @throws DarajaException if the request is unsuccessful.
     */
    public suspend fun transactionReversal(transactionReversalRequest: TransactionReversalRequest): TransactionReversalResponse =
        executeApiRequest(TRANSACTION_REVERSAL, transactionReversalRequest)

    /**
     * Dynamic QR - Generates a dynamic M-PESA QR Code.

     * Use this API to generate a Dynamic QR which enables Safaricom M-PESA customers who have My
     * Safaricom App or M-PESA app, to scan a QR (Quick Response) code, to capture till number and
     * amount then authorize to pay for goods and services at select LIPA NA M-PESA (LNM) merchant
     * outlets.

     * @param dynamicQrRequest The [DynamicQrRequest] request object.
     * @return A [DynamicQrResponse] object representing the successful response, or an error if the request fails.
     */
    public suspend fun generateDynamicQr(dynamicQrRequest: DynamicQrRequest): DynamicQrResponse =
        executeApiRequest(DYNAMIC_QR, dynamicQrRequest)

    private suspend inline fun <reified T : Any> executeApiRequest(
        apiEndpoint: String, requestBody: Any
    ): T = httpClient.post(apiEndpoint) { setBody(requestBody) }.bodyOrThrow<T>()
}

