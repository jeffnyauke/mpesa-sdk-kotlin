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

package util

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

fun <T : KotlinTarget> KotlinMultiplatformExtension.targetGroup(
    name: String,
    mainSourceSetTarget: KotlinSourceSet,
    testSourceSetTarget: KotlinSourceSet,
    vararg targets: T
): Pair<KotlinSourceSet, KotlinSourceSet> {
    val mainName = "${name}Main"
    val testName = "${name}Test"
    val main = sourceSets.maybeCreate(mainName).apply { dependsOn(mainSourceSetTarget) }
    val test = sourceSets.maybeCreate(testName).apply { dependsOn(testSourceSetTarget) }
    targets.forEach { target ->
        target.compilations["main"].defaultSourceSet { dependsOn(main) }
        target.compilations["test"].defaultSourceSet { dependsOn(test) }
    }
    return main to test
}

fun <T : KotlinTarget> KotlinMultiplatformExtension.targetGroup(
    name: String,
    mainSourceSetTarget: String,
    testSourceSetTarget: String,
    vararg targets: T
): Pair<KotlinSourceSet, KotlinSourceSet> = targetGroup(
    name = name,
    mainSourceSetTarget = sourceSets.getByName(mainSourceSetTarget),
    testSourceSetTarget = sourceSets.getByName(testSourceSetTarget),
    targets = targets,
)

fun NamedDomainObjectContainer<KotlinSourceSet>.withName(
    name: String,
    action: Action<KotlinSourceSet>
) {
    matching { it.name == name }.all(action)
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.sharedSourceSets(
    vararg sourceSets: String,
    action: Action<KotlinSourceSet>,
) {
    sourceSets.forEach { withName(it, action) }
}

fun NamedDomainObjectContainer<KotlinSourceSet>.jvmCommonMain(action: Action<KotlinSourceSet>) {
    sharedSourceSets("jvmCommonMain", "androidMain", action = action)
}

fun NamedDomainObjectContainer<KotlinSourceSet>.jvmCommonTest(action: Action<KotlinSourceSet>) {
    sharedSourceSets("jvmCommonTest", "androidUnitTest", action = action)
}

fun NamedDomainObjectContainer<KotlinSourceSet>.blockingMain(action: Action<KotlinSourceSet>) {
    sharedSourceSets("blockingMain", "androidMain", action = action)
}

fun NamedDomainObjectContainer<KotlinSourceSet>.blockingTest(action: Action<KotlinSourceSet>) {
    sharedSourceSets("blockingTest", "androidUnitTest", action = action)
}
