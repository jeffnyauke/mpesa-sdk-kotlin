package com.github.jeffnyauke.mpesa.logger

internal interface LoggerApi {
    fun logDWithTag(tag: String, message: String)
}