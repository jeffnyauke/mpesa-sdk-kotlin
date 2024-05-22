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

package com.github.jeffnyauke.mpesa.tests.mpesa

import com.github.jeffnyauke.mpesa.Mpesa
import com.github.jeffnyauke.mpesa.config.Environment
import com.github.jeffnyauke.mpesa.network.AccessToken
import com.github.jeffnyauke.mpesa.request.DynamicQrRequest
import com.github.jeffnyauke.mpesa.request.DynamicQrTransactionType
import com.github.jeffnyauke.mpesa.response.AuthorizationResponse
import com.github.jeffnyauke.mpesa.response.DynamicQrResponse
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

class MpesaAuthenticationTest : FunSpec({
    val consumerKey = "test_consumer_key"
    val consumerSecret = "test_consumer_secret"
    val firstAccessToken = "test_first_access_token"
    val secondAccessToken = "test_second_access_token"
    val expiresIn = 3600 // 1 hour in seconds

    test("Should successfully authenticate and obtain an access token") {
        runBlocking {
            var refreshed = false
            val mockEngine = MockEngine { request ->
                when (request.url.encodedPath) {
                    "/oauth/v1/generate" -> {
                        respond(
                            content = Json.encodeToString(
                                AuthorizationResponse(
                                    accessToken = firstAccessToken,
                                    expiresIn = expiresIn.toString()
                                )
                            ),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }

                    "/mpesa/qrcode/v1/generate" -> {
                        if (refreshed) {
                            respond(
                                content = Json.encodeToString(
                                    DynamicQrResponse(
                                        responseCode = "00",
                                        responseDescription = "Success",
                                        qrCode = "QRCode"
                                    )
                                ),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        } else {
                            refreshed = true
                            respond(
                                content = Json.encodeToString(
                                    DynamicQrResponse(
                                        responseCode = "00",
                                        responseDescription = "Success",
                                        qrCode = "QRCode"
                                    )
                                ),
                                status = HttpStatusCode.Unauthorized,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }
                    }

                    else -> error("Unhandled ${request.url.encodedPath}")
                }
            }
            val mpesa = Mpesa(
                consumerKey = consumerKey,
                consumerSecret = consumerSecret,
                environment = Environment.SANDBOX,
                httpClientEngine = mockEngine
            )

            // Make a request that requires authentication
            mpesa.generateDynamicQr(
                DynamicQrRequest(
                    merchantName = "MerchantName",
                    refNo = "RefNo",
                    amount = "Amount",
                    transactionType = DynamicQrTransactionType.BuyGoods,
                    cpi = "CPI"
                )
            )

            // Check that the access token was stored in the cache
            val cachedAccessToken = mpesa.cache.get(
                1L
            ) as AccessToken
            cachedAccessToken.value shouldBe firstAccessToken
            cachedAccessToken.expiryTime shouldBeGreaterThan Clock.System.now()
        }
    }

    test("Should refresh the access token when it expires") {
        runBlocking {
            var refreshed = false
            val secondMockEngine = MockEngine { request ->
                when (request.url.encodedPath) {
                    "/oauth/v1/generate" -> {
                        respond(
                            content = Json.encodeToString(
                                AuthorizationResponse(
                                    accessToken = secondAccessToken,
                                    expiresIn = expiresIn.toString()
                                )
                            ),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }

                    "/mpesa/qrcode/v1/generate" -> {
                        if (refreshed) {
                            respond(
                                content = Json.encodeToString(
                                    DynamicQrResponse(
                                        responseCode = "00",
                                        responseDescription = "Success",
                                        qrCode = "QRCode"
                                    )
                                ),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        } else {
                            refreshed = true
                            respond(
                                content = Json.encodeToString(
                                    DynamicQrResponse(
                                        responseCode = "00",
                                        responseDescription = "Success",
                                        qrCode = "QRCode"
                                    )
                                ),
                                status = HttpStatusCode.Unauthorized,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }
                    }

                    else -> error("Unhandled ${request.url.encodedPath}")
                }
            }

            // Set the expiry time of the access token to the past
            val mpesa = Mpesa(
                consumerKey = consumerKey,
                consumerSecret = consumerSecret,
                environment = Environment.SANDBOX,
                httpClientEngine = secondMockEngine
            )
            mpesa.cache.put(
                key = 1L,
                value = AccessToken(
                    value = firstAccessToken,
                    expiryTime = Clock.System.now().minus(expiresIn.toLong().seconds)
                )
            )

            // Make a request that requires authentication
            mpesa.generateDynamicQr(
                DynamicQrRequest(
                    merchantName = "MerchantName",
                    refNo = "RefNo",
                    amount = "Amount",
                    transactionType = DynamicQrTransactionType.BuyGoods,
                    cpi = "CPI"
                )
            )

            // Check that the access token was refreshed
            val cachedAccessToken = mpesa.cache.get(
                1L
            ) as AccessToken
            cachedAccessToken.value shouldBe secondAccessToken
            cachedAccessToken.expiryTime shouldBeGreaterThan Clock.System.now()
        }
    }
})
