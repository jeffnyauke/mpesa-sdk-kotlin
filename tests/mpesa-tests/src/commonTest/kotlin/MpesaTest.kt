package com.github.jeffnyauke.mpesa

import com.github.jeffnyauke.mpesa.config.Environment
import com.github.jeffnyauke.mpesa.exception.DarajaException
import com.github.jeffnyauke.mpesa.models.request.AccountBalanceOrganizationType
import com.github.jeffnyauke.mpesa.models.request.AccountBalanceRequest
import com.github.jeffnyauke.mpesa.models.request.B2cCommandId
import com.github.jeffnyauke.mpesa.models.request.B2cRequest
import com.github.jeffnyauke.mpesa.models.request.C2bCommandId
import com.github.jeffnyauke.mpesa.models.request.C2bRegisterRequest
import com.github.jeffnyauke.mpesa.models.request.C2bRegisterTransactionType
import com.github.jeffnyauke.mpesa.models.request.C2bRequest
import com.github.jeffnyauke.mpesa.models.request.DynamicQrRequest
import com.github.jeffnyauke.mpesa.models.request.DynamicQrTransactionType
import com.github.jeffnyauke.mpesa.models.request.StkPushQueryRequest
import com.github.jeffnyauke.mpesa.models.request.StkPushRequest
import com.github.jeffnyauke.mpesa.models.request.StkPushTransactionType
import com.github.jeffnyauke.mpesa.models.request.TransactionReversalReceiverOrganizationType
import com.github.jeffnyauke.mpesa.models.request.TransactionReversalRequest
import com.github.jeffnyauke.mpesa.models.request.TransactionStatusOrganizationType
import com.github.jeffnyauke.mpesa.models.request.TransactionStatusRequest
import com.github.jeffnyauke.mpesa.models.response.AccountBalanceResponse
import com.github.jeffnyauke.mpesa.models.response.B2cResponse
import com.github.jeffnyauke.mpesa.models.response.C2bRegisterResponse
import com.github.jeffnyauke.mpesa.models.response.C2bResponse
import com.github.jeffnyauke.mpesa.models.response.DynamicQrResponse
import com.github.jeffnyauke.mpesa.models.response.ErrorResponse
import com.github.jeffnyauke.mpesa.models.response.StkPushQueryResponse
import com.github.jeffnyauke.mpesa.models.response.StkPushResponse
import com.github.jeffnyauke.mpesa.models.response.TransactionReversalResponse
import com.github.jeffnyauke.mpesa.models.response.TransactionStatusResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
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
        val mpesa =
            Mpesa(consumerKey, consumerSecret, Environment.SANDBOX, httpClientEngine = mockEngine)

        test("dynamicQr should return successful response") {
            runBlocking {
                val request = DynamicQrRequest(
                    "MerchantName", "RefNo", "Amount", DynamicQrTransactionType.BuyGoods, "CPI"
                )
                val response = mpesa.dynamicQr(request)
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
                val response = mpesa.stkPush(request)
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
                val response = mpesa.stkPushQuery(request)
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
                val response = mpesa.c2bRegister(request)
                response.responseCode shouldBe "0"
            }
        }

        test("c2b should return successful response") {
            runBlocking {
                val request = C2bRequest(
                    "600984", C2bCommandId.CustomerPayBillOnline, "1", "254708374149", "NEF61H8J60J"
                )
                val response = mpesa.c2b(request)
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
                val response = mpesa.b2c(request)
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
                val response = mpesa.transactionStatus(request)
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
                val response = mpesa.accountBalance(request)
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
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/c2b/v1/registerurl" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/c2b/v1/simulate" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
                        status = HttpStatusCode.ServiceUnavailable,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/mpesa/b2c/v1/paymentrequest" -> {
                    respond(
                        content = Json.encodeToString(
                            ErrorResponse("1", "Some Error")
                        ),
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
        val mpesa =
            Mpesa(consumerKey, consumerSecret, Environment.SANDBOX, httpClientEngine = mockEngine)

        test("dynamicQr should throw DarajaException on error response") {
            runBlocking {
                val request = DynamicQrRequest(
                    "MerchantName", "RefNo", "Amount", DynamicQrTransactionType.BuyGoods, "CPI"
                )
                shouldThrow<DarajaException> { mpesa.dynamicQr(request) }
            }
        }

        test("stkPush should throw DarajaException on error response") {
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
                shouldThrow<DarajaException> { mpesa.stkPush(request) }
            }
        }

        test("stkPushQuery should throw DarajaException on error response") {
            runBlocking {
                val request = StkPushQueryRequest(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    "ws_CO_DMZ_123212312_2342347678234"
                )
                shouldThrow<DarajaException> { mpesa.stkPushQuery(request) }
            }
        }

        test("c2bRegister should throw DarajaException on error response") {
            runBlocking {
                val request = C2bRegisterRequest(
                    "600984",
                    C2bRegisterTransactionType.Completed,
                    "https://mydomain.com/confirmation",
                    "https://mydomain.com/validation"
                )
                shouldThrow<DarajaException> { mpesa.c2bRegister(request) }
            }
        }

        test("c2b should throw DarajaException on error response") {
            runBlocking {
                val request = C2bRequest(
                    "600984", C2bCommandId.CustomerPayBillOnline, "1", "254708374149", "NEF61H8J60J"
                )
                shouldThrow<DarajaException> { mpesa.c2b(request) }
            }
        }

        test("b2c should throw DarajaException on error response") {
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
                shouldThrow<DarajaException> { mpesa.b2c(request) }
            }
        }

        test("transactionStatus should throw DarajaException on error response") {
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
                shouldThrow<DarajaException> { mpesa.transactionStatus(request) }
            }
        }

        test("accountBalance should throw DarajaException on error response") {
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
                shouldThrow<DarajaException> { mpesa.accountBalance(request) }
            }
        }

        test("transactionReversal should throw DarajaException on error response") {
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
                shouldThrow<DarajaException> { mpesa.transactionReversal(request) }
            }
        }
    }
})
