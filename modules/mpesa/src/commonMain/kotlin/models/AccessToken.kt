package com.github.jeffnyauke.mpesa.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

public data class AccessToken(val value: String, val expiryTime: Instant)

public fun AccessToken.isExpired(): Boolean = expiryTime <= Clock.System.now()
