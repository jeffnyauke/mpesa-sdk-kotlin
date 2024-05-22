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

import Env.CI
import Env.SANDBOX
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.konan.target.HostManager
import util.buildHost
import util.enabled
import util.mainHost

plugins {
    id("convention.kotlin-mpp")
    id("convention.publishing")
    id("convention.publishing-dokka")
}

kotlin {
    fun configurePublishTasks(name: String, action: Action<in Task>) {
        publishing {
            publications.matching { it.name == name }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .all(action)
                tasks.withType<GenerateModuleMetadata>()
                    .matching { it.publication.orNull == targetPublication }
                    .all(action)
            }
        }
    }

    targets.all target@{
        configurePublishTasks(this@target.name) {
            onlyIf {
                !CI || this@target.enabled || (SANDBOX && this@target.buildHost == mainHost)
            }
        }

        if (this@target is KotlinAndroidTarget && (!CI || this@target.enabled || SANDBOX)) {
            this@target.publishLibraryVariants("release", "debug")
        }
    }

    configurePublishTasks("kotlinMultiplatform") {
        !CI || SANDBOX || HostManager.host == mainHost
    }
}
