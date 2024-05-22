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
    id("convention.base")
    `maven-publish`
}

publishing {
    publications {
        val ghOwnerId: String = project.properties["gh.owner.id"]!!.toString()
        val ghOwnerName: String = project.properties["gh.owner.name"]!!.toString()
        val ghOwnerEmail: String = project.properties["gh.owner.email"]!!.toString()
        withType<MavenPublication> {
            pom {
                name by project.name
                url by "https://github.com/$ghOwnerId/${rootProject.name}"
                description by provider { project.description }

                licenses {
                    license {
                        name by "The Apache License, Version 2.0"
                        url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                developers {
                    developer {
                        id by ghOwnerId
                        name by ghOwnerName
                        email by ghOwnerEmail
                    }
                }

                scm {
                    connection by "scm:git:git://github.com:$ghOwnerId/${rootProject.name}.git"
                    developerConnection.set("scm:git:git@github.com:$ghOwnerId/${rootProject.name}.git")
                    url by "https://github.com/$ghOwnerId/${rootProject.name}.git"
                    tag by Git.headCommitHash
                }
            }
        }
    }
}
