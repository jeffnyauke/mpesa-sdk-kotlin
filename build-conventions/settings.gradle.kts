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
