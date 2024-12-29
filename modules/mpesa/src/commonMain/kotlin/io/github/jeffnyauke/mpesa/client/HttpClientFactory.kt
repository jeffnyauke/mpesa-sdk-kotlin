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

package io.github.jeffnyauke.mpesa.client

import io.github.jeffnyauke.mpesa.config.Constants.DEFAULT_LOG_TAG
import io.github.jeffnyauke.mpesa.config.Constants.Endpoints.ACCESS_TOKEN
import io.github.jeffnyauke.mpesa.config.Environment
import io.github.jeffnyauke.mpesa.logger.MpesaLogger
import io.github.jeffnyauke.mpesa.logger.MpesaLoggerImpl
import io.github.jeffnyauke.mpesa.responses.AuthorizationResponse
import io.github.jeffnyauke.mpesa.store.AccessToken
import io.github.jeffnyauke.mpesa.store.BaseAuthStore
import io.github.jeffnyauke.mpesa.utils.encodeBase64
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmSynthetic
import kotlin.time.Duration.Companion.seconds

internal class HttpClientFactory(
    private val authMutex: Mutex = Mutex(),
    private val jsonParser: Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    },
    private val mpesaLogger: MpesaLogger = MpesaLoggerImpl(),
) {
    @JvmSynthetic
    internal fun create(
        httpClientEngine: HttpClientEngine? = null,
        consumerKey: String,
        consumerSecret: String,
        environment: Environment,
        shouldEnableLogging: Boolean,
        authStore: BaseAuthStore
    ): HttpClient {
        val httpClientConfig = httpClientConfigProvider(
            consumerKey,
            consumerSecret,
            environment,
            shouldEnableLogging,
            authStore
        )

        return httpClientEngine?.let { HttpClient(it, httpClientConfig) } ?: HttpClient(
            httpClientConfig
        )
    }

    private fun httpClientConfigProvider(
        consumerKey: String,
        consumerSecret: String,
        environment: Environment,
        shouldEnableLogging: Boolean,
        authStore: BaseAuthStore
    ): HttpClientConfig<*>.() -> Unit {
        return {
            expectSuccess = true
            addDefaultResponseValidation()
            configureAuthentication(consumerKey, consumerSecret, authStore)
            installContentNegotiation()
            configureLogging(shouldEnableLogging)
            configureDefaultRequest(environment)
        }
    }

    private fun HttpClientConfig<*>.configureAuthentication(
        consumerKey: String,
        consumerSecret: String,
        authStore: BaseAuthStore
    ) {
        install(Auth) {
            bearer {
                loadTokens {
                    authStore.token?.value?.let { token -> BearerTokens(token, token) }
                }
                refreshTokens {
                    authMutex.withLock {
                        authStore.clear()
                        val credentials = "$consumerKey:$consumerSecret".encodeBase64()
                        val refreshTokenResponse = client.get(ACCESS_TOKEN) {
                            header(Authorization, "Basic $credentials")
                            markAsRefreshTokenRequest()
                        }.body<AuthorizationResponse>()
                        with(refreshTokenResponse) {
                            val expiryTime = Clock.System.now().plus(expiresIn.toInt().seconds)
                            authStore.save(AccessToken(accessToken, expiryTime))
                            BearerTokens(accessToken, accessToken)
                        }
                    }
                }
            }
        }
    }

    private fun HttpClientConfig<*>.installContentNegotiation() {
        install(ContentNegotiation) { json(jsonParser) }
    }

    private fun HttpClientConfig<*>.configureLogging(shouldEnableLogging: Boolean) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    mpesaLogger.d(DEFAULT_LOG_TAG, message)
                }
            }
            level = when (shouldEnableLogging) {
                true -> LogLevel.ALL
                false -> LogLevel.NONE
            }
        }
    }

    private fun HttpClientConfig<*>.configureDefaultRequest(environment: Environment) {
        defaultRequest {
            url.host = environment.host
            url.protocol = URLProtocol.HTTPS
            contentType(ContentType.Application.Json)
        }
    }
}
