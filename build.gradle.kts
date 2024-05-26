import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

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
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.maven.publish)
    id("convention.versions")
    id("convention.git-hooks")
    id("convention.kotlin-mpp-tier3")
    id("convention.library-android")
    id("convention.library-mpp")
    id("convention.publishing-mpp")
}

nexusPublishing.repositories {
    sonatype {
        nexusUrl by uri("https://s01.oss.sonatype.org/service/local/")
        snapshotRepositoryUrl by uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    configure(KotlinMultiplatform(
        javadocJar = JavadocJar.Dokka("dokkaHtml"),
        sourcesJar = true,
        androidVariantsToPublish = listOf("debug", "release")
    ))
    signAllPublications()
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                subprojects.filter { it.path.startsWith(":modules:") }.forEach { api(it) }
            }
        }
    }
}
