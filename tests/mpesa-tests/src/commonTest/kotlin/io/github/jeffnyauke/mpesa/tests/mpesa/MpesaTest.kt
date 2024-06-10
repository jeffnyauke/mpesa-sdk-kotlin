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

package io.github.jeffnyauke.mpesa.tests.mpesa

import io.github.jeffnyauke.mpesa.config.Environment.SANDBOX
import io.github.jeffnyauke.mpesa.requests.AccountBalanceOrganizationType
import io.github.jeffnyauke.mpesa.requests.AccountBalanceRequest
import io.github.jeffnyauke.mpesa.requests.B2cCommandId
import io.github.jeffnyauke.mpesa.requests.B2cRequest
import io.github.jeffnyauke.mpesa.requests.C2bCommandId
import io.github.jeffnyauke.mpesa.requests.C2bRegisterRequest
import io.github.jeffnyauke.mpesa.requests.C2bRegisterTransactionType
import io.github.jeffnyauke.mpesa.requests.C2bRequest
import io.github.jeffnyauke.mpesa.requests.DynamicQrRequest
import io.github.jeffnyauke.mpesa.requests.DynamicQrTransactionType
import io.github.jeffnyauke.mpesa.requests.StkPushQueryRequest
import io.github.jeffnyauke.mpesa.requests.StkPushRequest
import io.github.jeffnyauke.mpesa.requests.StkPushTransactionType
import io.github.jeffnyauke.mpesa.requests.TransactionReversalReceiverOrganizationType
import io.github.jeffnyauke.mpesa.requests.TransactionReversalRequest
import io.github.jeffnyauke.mpesa.requests.TransactionStatusOrganizationType
import io.github.jeffnyauke.mpesa.requests.TransactionStatusRequest
import io.github.jeffnyauke.mpesa.responses.AccountBalanceResponse
import io.github.jeffnyauke.mpesa.responses.B2cResponse
import io.github.jeffnyauke.mpesa.responses.C2bRegisterResponse
import io.github.jeffnyauke.mpesa.responses.C2bResponse
import io.github.jeffnyauke.mpesa.responses.DynamicQrResponse
import io.github.jeffnyauke.mpesa.responses.ErrorResponse
import io.github.jeffnyauke.mpesa.responses.StkPushQueryResponse
import io.github.jeffnyauke.mpesa.responses.StkPushResponse
import io.github.jeffnyauke.mpesa.responses.TransactionReversalResponse
import io.github.jeffnyauke.mpesa.responses.TransactionStatusResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MpesaTest : FunSpec({
    context("success responses") {
        val mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/mpesa/stkpush/v1/processrequest" -> {
                    respond(
                        content = Json.encodeToString(
                            StkPushResponse(
                                "merchantRequestId",
                                "checkoutRequestId",
                                "0",
                                "Success",
                                "CustomerMessage"
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/stkpushquery/v1/query" -> {
                    respond(
                        content = Json.encodeToString(
                            StkPushQueryResponse(
                                "merchantRequestId",
                                "checkoutRequestId",
                                "0",
                                "ResultDesc",
                                "ResponseDescription",
                                "0"
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/c2b/v1/registerurl" -> {
                    respond(
                        content = Json.encodeToString(
                            C2bRegisterResponse(
                                "conversationId",
                                "originatorConversationId",
                                "0",
                                "ResponseDescription"
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/c2b/v1/simulate" -> {
                    respond(
                        content = Json.encodeToString(
                            C2bResponse(
                                "conversationId",
                                "originatorConversationId",
                                "0",
                                "ResponseDescription"
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/b2c/v1/paymentrequest" -> {
                    respond(
                        content = Json.encodeToString(
                            B2cResponse(
                                "conversationId",
                                "originatorConversationId",
                                "0",
                                "ResponseDescription"
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/transactionstatus/v1/query" -> {
                    respond(
                        content = Json.encodeToString(
                            TransactionStatusResponse(
                                "conversationId",
                                "originatorConversationId",
                                "0",
                                "ResponseDescription"
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/accountbalance/v1/query" -> {
                    respond(
                        content = Json.encodeToString(
                            AccountBalanceResponse(
                                "conversationId",
                                "originatorConversationId",
                                "0",
                                "ResponseDescription"
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/reversal/v1/request" -> {
                    respond(
                        content = Json.encodeToString(
                            TransactionReversalResponse(
                                "conversationId",
                                "originatorConversationId",
                                "0",
                                "ResponseDescription"
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/qrcode/v1/generate" -> {
                    respond(
                        content = Json.encodeToString(DynamicQrResponse("00", "Success", "QRCode")),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }

        val consumerKey = "test_consumer_key"
        val consumerSecret = "test_consumer_secret"
        val mpesa = io.github.jeffnyauke.mpesa.Mpesa(
            consumerKey,
            consumerSecret,
            SANDBOX,
            httpClientEngine = mockEngine
        )

        test("dynamicQr should return successful response") {
            runBlocking {
                val request = DynamicQrRequest(
                    "MerchantName",
                    "RefNo",
                    "Amount",
                    DynamicQrTransactionType.BuyGoods,
                    "CPI"
                )
                val response = mpesa.generateDynamicQr(request)
                response.responseCode shouldBe "00"
                response.responseDescription shouldBe "Success"
            }
        }

        test("stkPush should return successful response") {
            runBlocking {
                val request = StkPushRequest(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    transactionType = StkPushTransactionType.CustomerPayBillOnline,
                    amount = "1",
                    phoneNumber = "254708374149",
                    partyA = "254708374149",
                    partyB = "174379",
                    callBackUrl = "https://mydomain.com/path",
                    accountReference = "CompanyXLTD",
                    transactionDesc = "Payment of X",
                )
                val response = mpesa.initiateStkPush(request)
                response.responseCode shouldBe "0"
                response.responseDescription shouldBe "Success"
            }
        }

        test("stkPushQuery should return successful response") {
            runBlocking {
                val request = StkPushQueryRequest(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    "ws_CO_DMZ_123212312_2342347678234"
                )
                val response = mpesa.queryStkPushStatus(request)
                response.responseCode shouldBe "0"
                response.resultCode shouldBe "0"
            }
        }

        test("c2bRegister should return successful response") {
            runBlocking {
                val request = C2bRegisterRequest(
                    "600984",
                    C2bRegisterTransactionType.Completed,
                    "https://mydomain.com/confirmation",
                    "https://mydomain.com/validation"
                )
                val response = mpesa.registerC2bUrls(request)
                response.responseCode shouldBe "0"
            }
        }

        test("c2b should return successful response") {
            runBlocking {
                val request = C2bRequest(
                    "600984",
                    C2bCommandId.CustomerPayBillOnline,
                    "1",
                    "254708374149",
                    "NEF61H8J60J"
                )
                val response = mpesa.initiateC2bTransaction(request)
                response.responseCode shouldBe "0"
            }
        }

        test("b2c should return successful response") {
            runBlocking {
                val request = B2cRequest(
                    "testapi",
                    "securityCredential",
                    B2cCommandId.SalaryPayment,
                    "1",
                    "600999",
                    "254708374149",
                    "https://mydomain.com/b2c/result",
                    "https://mydomain.com/b2c/queue",
                    "Test Remarks",
                    ""
                )
                val response = mpesa.initiateB2cTransaction(request)
                response.responseCode shouldBe "0"
            }
        }

        test("transactionStatus should return successful response") {
            runBlocking {
                val request = TransactionStatusRequest(
                    "testapi",
                    "securityCredential",
                    "NEF61H8J60",
                    "AG_20190826_0000777ab7d848b9e721",
                    "600999",
                    TransactionStatusOrganizationType.Shortcode,
                    "https://mydomain.com/TransactionStatus/queue/",
                    "https://mydomain.com/TransactionStatus/result/",
                    "Test Remarks",
                    ""
                )
                val response = mpesa.getTransactionStatus(request)
                response.responseCode shouldBe "0"
            }
        }

        test("accountBalance should return successful response") {
            runBlocking {
                val request = AccountBalanceRequest(
                    "testapi",
                    "securityCredential",
                    "600983",
                    AccountBalanceOrganizationType.Shortcode,
                    "https://mydomain.com/AccountBalance/result/",
                    "https://mydomain.com/AccountBalance/queue/",
                    ""
                )
                val response = mpesa.getAccountBalance(request)
                response.responseCode shouldBe "0"
            }
        }

        test("transactionReversal should return successful response") {
            runBlocking {
                val request = TransactionReversalRequest(
                    "testapi",
                    "securityCredential",
                    "OEI2AK4Q16",
                    "1",
                    "600980",
                    TransactionReversalReceiverOrganizationType.Default,
                    "https://mydomain.com/Reversal/result/",
                    "https://mydomain.com/Reversal/queue/",
                    "Test Remarks",
                    ""
                )
                val response = mpesa.transactionReversal(request)
                response.responseCode shouldBe "0"
            }
        }
    }

    context("error responses") {
        val mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/mpesa/stkpush/v1/processrequest" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/stkpushquery/v1/query" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.BadGateway,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/c2b/v1/registerurl" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.RequestTimeout,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/c2b/v1/simulate" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.MovedPermanently,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/b2c/v1/paymentrequest" -> {
                    respond(
                        content = Json.encodeToString("Normal string"),
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/transactionstatus/v1/query" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/accountbalance/v1/query" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/reversal/v1/request" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/qrcode/v1/generate" -> {
                    respond(
                        content = Json.encodeToString(ErrorResponse("1", "Some Error")),
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }

        val consumerKey = "test_consumer_key"
        val consumerSecret = "test_consumer_secret"
        val mpesa = io.github.jeffnyauke.mpesa.Mpesa(
            consumerKey,
            consumerSecret,
            SANDBOX,
            httpClientEngine = mockEngine
        )

        test("dynamicQr should throw ResponseException on error response") {
            runBlocking {
                val request = DynamicQrRequest(
                    "MerchantName",
                    "RefNo",
                    "Amount",
                    DynamicQrTransactionType.BuyGoods,
                    "CPI"
                )
                shouldThrow<ResponseException> {
                    mpesa.generateDynamicQr(
                        request
                    )
                }
            }
        }

        test("stkPush should throw ResponseException on error response") {
            runBlocking {
                val request = StkPushRequest(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    transactionType = StkPushTransactionType.CustomerPayBillOnline,
                    amount = "1",
                    phoneNumber = "254708374149",
                    partyA = "254708374149",
                    partyB = "174379",
                    callBackUrl = "https://mydomain.com/path",
                    accountReference = "CompanyXLTD",
                    transactionDesc = "Payment of X",
                )
                shouldThrow<ResponseException> {
                    mpesa.initiateStkPush(
                        request
                    )
                }
            }
        }

        test("stkPushQuery should throw ResponseException on error response") {
            runBlocking {
                val request = StkPushQueryRequest(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    "ws_CO_DMZ_123212312_2342347678234"
                )
                shouldThrow<ResponseException> {
                    mpesa.queryStkPushStatus(
                        request
                    )
                }
            }
        }

        test("c2bRegister should throw ResponseException on error response") {
            runBlocking {
                val request = C2bRegisterRequest(
                    "600984",
                    C2bRegisterTransactionType.Completed,
                    "https://mydomain.com/confirmation",
                    "https://mydomain.com/validation"
                )
                shouldThrow<ResponseException> {
                    mpesa.registerC2bUrls(
                        request
                    )
                }
            }
        }

        test("c2b should throw ResponseException on error response") {
            runBlocking {
                val request = C2bRequest(
                    "600984",
                    C2bCommandId.CustomerPayBillOnline,
                    "1",
                    "254708374149",
                    "NEF61H8J60J"
                )
                shouldThrow<ResponseException> {
                    mpesa.initiateC2bTransaction(
                        request
                    )
                }
            }
        }

        test("b2c should throw ResponseException on error response") {
            runBlocking {
                val request = B2cRequest(
                    "testapi",
                    "securityCredential",
                    B2cCommandId.SalaryPayment,
                    "1",
                    "600999",
                    "254708374149",
                    "https://mydomain.com/b2c/result",
                    "https://mydomain.com/b2c/queue",
                    "Test Remarks",
                    ""
                )
                shouldThrow<ResponseException> {
                    mpesa.initiateB2cTransaction(
                        request
                    )
                }
            }
        }

        test("transactionStatus should throw ResponseException on error response") {
            runBlocking {
                val request = TransactionStatusRequest(
                    "testapi",
                    "securityCredential",
                    "NEF61H8J60",
                    "AG_20190826_0000777ab7d848b9e721",
                    "600999",
                    TransactionStatusOrganizationType.Shortcode,
                    "https://mydomain.com/TransactionStatus/queue/",
                    "https://mydomain.com/TransactionStatus/result/",
                    "Test Remarks",
                    ""
                )
                shouldThrow<ResponseException> {
                    mpesa.getTransactionStatus(
                        request
                    )
                }
            }
        }

        test("accountBalance should throw ResponseException on error response") {
            runBlocking {
                val request = AccountBalanceRequest(
                    "testapi",
                    "securityCredential",
                    "600983",
                    AccountBalanceOrganizationType.Shortcode,
                    "https://mydomain.com/AccountBalance/result/",
                    "https://mydomain.com/AccountBalance/queue/",
                    ""
                )
                shouldThrow<ResponseException> {
                    mpesa.getAccountBalance(
                        request
                    )
                }
            }
        }

        test("transactionReversal should throw ResponseException on error response") {
            runBlocking {
                val request = TransactionReversalRequest(
                    "testapi",
                    "securityCredential",
                    "OEI2AK4Q16",
                    "1",
                    "600980",
                    TransactionReversalReceiverOrganizationType.Default,
                    "https://mydomain.com/Reversal/result/",
                    "https://mydomain.com/Reversal/queue/",
                    "Test Remarks",
                    ""
                )
                shouldThrow<ResponseException> {
                    mpesa.transactionReversal(
                        request
                    )
                }
            }
        }
    }
})
