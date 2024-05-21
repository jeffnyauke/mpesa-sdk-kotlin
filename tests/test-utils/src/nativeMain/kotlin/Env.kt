package test

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKStringFromUtf8
import platform.posix.getenv

actual object Env {
  @OptIn(ExperimentalForeignApi::class)
  actual operator fun get(key: String): String? = getenv(key)?.toKStringFromUtf8()
}
