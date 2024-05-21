pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

plugins {
  id("com.gradle.develocity") version "3.17.3"
}

develocity {
    val runsOnCI = providers.environmentVariable("CI").getOrElse("false").toBoolean()
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"
        uploadInBackground = !runsOnCI
    }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "mpesa-sdk-kotlin"

includeBuild("./build-conventions/")
includeBuild("./sandbox/")

include(
  ":modules:mpesa",
  ":tests:mpesa-tests",
    ":tests:test-utils"
)
