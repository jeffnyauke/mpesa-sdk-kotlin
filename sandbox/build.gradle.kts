plugins {
  id("convention.jvm")
  kotlin("jvm") version libs.versions.kotlin.get()
  application
}

description = "Local consumer sandbox"

application {
  mainClass.set("local.sandbox.MainKt")
}

dependencies {
  implementation(libs.kotlinx.coroutines.core)
  implementation("com.github.jeffnyauke:mpesa")
  testImplementation("com.github.jeffnyauke:test-utils")
}
