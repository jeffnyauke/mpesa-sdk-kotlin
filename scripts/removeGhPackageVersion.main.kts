#!/usr/bin/env kotlin

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

// usage: ./removeGhPackageVersion.main.kts mpesa 1.1.3

@file:DependsOn("io.ktor:ktor-client-cio-jvm:2.3.0")
@file:DependsOn("io.ktor:ktor-client-content-negotiation-jvm:2.3.0")
@file:DependsOn("io.ktor:ktor-serialization-gson-jvm:2.3.0")
@file:DependsOn("io.ktor:ktor-client-auth-jvm:2.3.0")

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.runBlocking

val targetRepo = args.getOrNull(0) ?: error("Target repository not specified")
val targetVersion = args.getOrNull(1) ?: error("Target version not specified")
println("TargetRepo[$targetRepo] TargetVersion[$targetVersion]")

val ghClient = HttpClient(CIO) {
    install(Auth) {
        basic {
            credentials {
                BasicAuthCredentials(
                    username = System.getenv("GITHUB_LOGIN"),
                    password = System.getenv("GITHUB_OAUTH")
                )
            }
            sendWithoutRequest { true }
        }
    }
    install(ContentNegotiation) {
        gson()
    }
    followRedirects = true
    defaultRequest {
        host = "api.github.com"
        url {
            protocol = URLProtocol.HTTPS
        }
        header("Accept", "application/vnd.github.v3+json")
    }
}

data class GHRepository(
    val id: String,
    val nodeId: String,
    val name: String,
    val fullName: String
)

data class GHPackage(
    val id: String,
    val name: String,
    val packageType: String,
    val repository: GHRepository
)

data class GHPackageVersion(val id: String, val name: String)

runBlocking {
    val packages = mutableListOf<GHPackage>().run {
        var i = 0
        while (true) {
            val page = ghClient.get("/user/packages") {
                parameter("package_type", "maven")
                parameter("page", "${++i}")
            }.body<List<GHPackage>>()
            if (page.isEmpty()) {
                break
            } else {
                addAll(page.filter { it.repository.name == targetRepo })
            }
        }
        toList()
    }
    val versions = packages.map {
        it to mutableListOf<GHPackageVersion>().run {
            var i = 0
            while (true) {
                val page = ghClient.get("/user/packages/${it.packageType}/${it.name}/versions") {
                    parameter("package_type", "maven")
                    parameter("page", "${++i}")
                }.body<List<GHPackageVersion>>()
                if (page.isEmpty()) {
                    break
                } else {
                    addAll(page)
                }
            }
            toList()
        }
    }
    versions.forEach { (pkg, ver) ->
        if (ver.size > 1) {
            ver.firstOrNull { it.name == targetVersion }?.let {
                println("Deleting ${pkg.name}@${it.name}")
                ghClient.delete("/user/packages/${pkg.packageType}/${pkg.name}/versions/${it.id}")
            }
        } else if (ver.any { it.name == targetVersion }) {
            println("Deleting ${pkg.name}")
            ghClient.delete("/user/packages/${pkg.packageType}/${pkg.name}")
        }
    }
}
