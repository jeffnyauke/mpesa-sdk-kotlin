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
    id("convention.kotlin-mpp-tier2")
}

// https://kotlinlang.org/docs/native-target-support.html#tier-3
kotlin {
//  @OptIn(ExperimentalWasmDsl::class)
//  wasm {
//    browser {
//      commonWebpackConfig {
//        cssSupport { enabled.set(true) }
//        scssSupport { enabled.set(true) }
//      }
//      testTask {
//        useKarma {
//          useChrome()
//        }
//      }
//    }
//  }
    // androidNativeArm32()
    // androidNativeArm64()
    // androidNativeX86()
    // androidNativeX64()
    mingwX64()
    // watchosDeviceArm64()
}
