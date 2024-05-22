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

package com.github.jeffnyauke.mpesa.network

import com.github.jeffnyauke.mpesa.config.Environment
import com.github.jeffnyauke.mpesa.constants.Cache.ACCESS_TOKEN_KEY
import com.github.jeffnyauke.mpesa.constants.Network.Endpoints.ACCESS_TOKEN
import com.github.jeffnyauke.mpesa.constants.Network.NetworkApiConfig.CONNECT_TIMEOUT_MILLIS
import com.github.jeffnyauke.mpesa.constants.Network.NetworkApiConfig.DEFAULT_LOG_TAG
import com.github.jeffnyauke.mpesa.constants.Network.NetworkApiConfig.REQUEST_TIMEOUT_MILLIS
import com.github.jeffnyauke.mpesa.constants.Network.NetworkApiConfig.SOCKET_TIMEOUT_MILLIS
import com.github.jeffnyauke.mpesa.exception.DarajaException
import com.github.jeffnyauke.mpesa.logger.LoggerApiImpl
import com.github.jeffnyauke.mpesa.response.AuthorizationResponse
import com.github.jeffnyauke.mpesa.response.DetailedErrorResponse
import com.github.jeffnyauke.mpesa.response.ErrorResponse
import com.github.jeffnyauke.mpesa.util.encodeBase64
import io.github.reactivecircus.cache4k.Cache
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
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
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmSynthetic
import kotlin.time.Duration.Companion.seconds

private val JSON = Json {
    isLenient = true
    ignoreUnknownKeys = true
}

private val mpesaLogger = LoggerApiImpl()

private val authMutex by lazy { Mutex() }

@JvmSynthetic
public fun createHttpClient(
    httpClientEngine: HttpClientEngine? = null,
    httpClientConfig: HttpClientConfig<*>.() -> Unit
): HttpClient = if (httpClientEngine == null) {
    HttpClient {
        httpClientConfig()
    }
} else {
    HttpClient(httpClientEngine) {
        httpClientConfig()
    }
}

public fun commonKtorConfiguration(
    consumerKey: String,
    consumerSecret: String,
    environment: Environment,
    shouldEnableLogging: Boolean,
    cache: Cache<Long, AccessToken>
): HttpClientConfig<*>.() -> Unit = {
    install(Auth) {
        bearer {
            loadTokens {
                cache.get(ACCESS_TOKEN_KEY)
                    ?.let { accessToken -> BearerTokens(accessToken.value, accessToken.value) }
            }
            refreshTokens {
                authMutex.withLock {
                    cache.invalidate(ACCESS_TOKEN_KEY)

                    val credentials = "$consumerKey:$consumerSecret".encodeBase64()
                    val refreshTokenResponse = client.get(ACCESS_TOKEN) {
                        header(Authorization, "Basic $credentials")
                        markAsRefreshTokenRequest()
                    }.bodyOrThrow<AuthorizationResponse>()

                    with(refreshTokenResponse) {
                        cache.put(
                            ACCESS_TOKEN_KEY,
                            AccessToken(
                                accessToken,
                                Clock.System.now().plus(expiresIn.toInt().seconds)
                            )
                        )
                        BearerTokens(
                            accessToken,
                            accessToken
                        )
                    }
                }
            }
        }
    }

    install(ContentNegotiation) { json(JSON) }

    install(HttpTimeout) {
        requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
        socketTimeoutMillis = SOCKET_TIMEOUT_MILLIS
        connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                mpesaLogger.logDWithTag(DEFAULT_LOG_TAG, message)
            }
        }
        level = when (shouldEnableLogging) {
            true -> LogLevel.ALL
            false -> LogLevel.NONE
        }
    }

    defaultRequest {
        url.host = environment.host
        url.protocol = URLProtocol.HTTPS
        contentType(ContentType.Application.Json)
    }

    addDefaultResponseValidation()
}

public data class AccessToken(val value: String, val expiryTime: Instant)

public fun AccessToken.isExpired(): Boolean = expiryTime <= Clock.System.now()

internal suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
    if (status != OK) {
        val errorResponse = runCatching {
            JSON.decodeFromString<DetailedErrorResponse>(bodyAsText())
        }.getOrElse {
            ErrorResponse(status.value.toString(), status.description)
        }
        throw DarajaException(errorResponse)
    }
    return body()
}
