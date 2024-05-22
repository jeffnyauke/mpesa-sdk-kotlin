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

plugins {
    id("convention.kotlin-mpp-tier1")
    id("convention.library-android")
    id("kotlinx-serialization")
    id("io.kotest.multiplatform")
}

kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation(project(":tests:test-utils"))
                implementation(project(":modules:mpesa"))
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.datetime)
                implementation(libs.cache4k)
                implementation(libs.bundles.kotest)
            }
        }
    }
}
