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

@file:Suppress("PackageDirectoryMismatch")

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.the
import java.nio.charset.Charset

internal inline val Project.libs get() = the<LibrariesForLibs>()

infix fun <T> Property<T>.by(value: T) = set(value)
infix fun <T> Property<T>.by(value: Provider<T>) = set(value)

object Git {
    val headCommitHash by lazy {
        val processBuilder = ProcessBuilder("git", "rev-parse", "--verify", "HEAD")
        val child = processBuilder.start()
        child.waitFor()
        child.inputStream.readAllBytes().toString(Charset.defaultCharset()).trim()
    }
}

object Env {
    val CI = System.getenv("CI") !in arrayOf(null, "0", "false", "n", "N")
    val SANDBOX by lazy { !"false".equals(System.getenv("SANDBOX") ?: "false", true) }
}
