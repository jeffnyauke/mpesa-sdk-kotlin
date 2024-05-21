package com.github.jeffnyauke.mpesa.logger

import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter

internal class LoggerApiImpl : LoggerApi {

    init {
        Logger.setLogWriters(platformLogWriter())
    }

    override fun logDWithTag(tag: String, message: String) {
        Logger.withTag(tag).d(message)
    }
}