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
    id("convention.pom")
    `maven-publish`
    signing
}

tasks {
    withType<Jar> {
        manifest {
            attributes += sortedMapOf(
                "Built-By" to System.getProperty("user.name"),
                "Build-Jdk" to System.getProperty("java.version"),
                "Implementation-Version" to project.version,
                "Created-By" to "${GradleVersion.current()}",
                "Created-From" to Git.headCommitHash
            )
        }
    }
    val cleanMavenLocal by registering {
        group = "build"
        doLast {
            val groupRepo =
                file(
                    "${System.getProperty("user.home")}/.m2/repository/${
                        project.group.toString().replace(".", "/")
                    }"
                )
            publishing.publications.filterIsInstance<MavenPublication>().forEach {
                groupRepo.resolve(it.artifactId).deleteRecursively()
            }
        }
    }
    named("clean") {
        dependsOn(cleanMavenLocal)
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    if (signingKey != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}

publishing {
    publications {
        val ghOwnerId: String = project.properties["gh.owner.id"]!!.toString()
        repositories {
            maven("https://maven.pkg.github.com/$ghOwnerId/${rootProject.name}") {
                name = "GitHub"
                credentials {
                    username = System.getenv("GH_USERNAME")
                    password = System.getenv("GH_PASSWORD")
                }
            }
        }
    }
}
