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
