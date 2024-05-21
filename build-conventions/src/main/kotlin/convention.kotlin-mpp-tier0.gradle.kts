import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import util.enabled

plugins {
  id("convention.jvm")
  id("convention.kotlin-mpp")
}

plugins.withId("com.android.library") {
  configure<KotlinMultiplatformExtension> {
    androidTarget()
  }
}

kotlin {
  js {
    useCommonJs()
    browser {
      commonWebpackConfig {
        cssSupport { enabled.set(true) }
        scssSupport { enabled.set(true) }
      }
      testTask {
        enabled = false
        useKarma()
      }
    }
  }

  jvm()
}
