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

import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("convention.base")
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

detekt {
    ignoreFailures = true
    parallel = true
    isIgnoreFailures = true
    autoCorrect = false
    buildUponDefaultConfig = true
    basePath = rootProject.projectDir.path
}

tasks.withType<Detekt>().configureEach {
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")

    reports {
        // observe findings in your browser with structure and code snippets
        html.required.set(true)
        // checkstyle like format mainly for integrations like Jenkins
        xml.required.set(true)
        // similar to the console output, contains issue signature to manually edit baseline files
        txt.required.set(true)
        // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations
        // with Github Code Scanning
        sarif.required.set(true)
    }
}
