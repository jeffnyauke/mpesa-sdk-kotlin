![Banner](/docs/Writerside/images/mpesa_sdk_kotlin_white.png)
[![Build](https://github.com/jeffnyauke/mpesa-sdk-kotlin/actions/workflows/check.yml/badge.svg)](https://github.com/jeffnyauke/mpesa-kmp-library/actions/workflows/check.yml)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.22-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jeffnyauke/mpesa-sdk-kotlin?color=blue)](https://search.maven.org/search?q=g:io.github.jeffnyauke.mpesa)

[//]: # (![badge-android]&#40;http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat&#41;)

[//]: # (![badge-ios]&#40;http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat&#41;)

[//]: # (![badge-mac]&#40;http://img.shields.io/badge/platform-macos-111111.svg?style=flat&#41;)

[//]: # (![badge-watchos]&#40;http://img.shields.io/badge/platform-watchos-C0C0C0.svg?style=flat&#41;)

[//]: # (![badge-tvos]&#40;http://img.shields.io/badge/platform-tvos-808080.svg?style=flat&#41;)

[//]: # (![badge-jvm]&#40;http://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat&#41;)

[//]: # (![badge-linux]&#40;http://img.shields.io/badge/platform-linux-2D3F6C.svg?style=flat&#41;)

[//]: # (![badge-windows]&#40;http://img.shields.io/badge/platform-windows-4D76CD.svg?style=flat&#41;)

[//]: # (![badge-nodejs]&#40;https://img.shields.io/badge/platform-jsNode-F8DB5D.svg?style=flat&#41;)

[//]: # (![badge-browser]&#40;https://img.shields.io/badge/platform-jsBrowser-F8DB5D.svg?style=flat&#41;)

## Introduction

This SDK provides a convenient way to interact with the Safaricom M-Pesa Daraja API v2.0 from your Kotlin Multiplatform projects.

**Features:**

* Supports all major Daraja API endpoints, including:
    * 🤳 Dynamic QR
    * 💶 STK Push - Lipa na M-Pesa Online API (M-PESA express)
    * ⏳ STK Push query
    * 📝 C2B register
    * 💶 C2B
    * 💶 B2C
    * ⏳ Transaction status
    * 🏦 Account balance
    * 🔁 Transaction reversal
* Built with Kotlin Multiplatform, allowing you to use the same codebase across different platforms (Android, iOS, JVM, JS, Native).
* Provides a simple and intuitive API surface.
* Handles authentication and token management.

## Documentation

For detailed documentation, API reference, and usage examples, please visit the [official website](https://jeffnyauke.github.io/mpesa-sdk-kotlin/).

## Installation

The library is available on Maven Central. Latest version [![Maven Central](https://img.shields.io/maven-central/v/com.github.jeffnyauke/mpesa-sdk-kotlin?color=blue)](https://search.maven.org/search?q=g:com.github.jeffnyauke.mpesa)

```kotlin
dependencies {
    implementation("io.github.jeffnyauke:mpesa:x.y.z")
}
```

## Quick Start

```kotlin
val mpesa = Mpesa(
    consumerKey = "YOUR_CONSUMER_KEY",
    consumerSecret = "YOUR_CONSUMER_SECRET",
    environment = Environment.SANDBOX // or Environment.PRODUCTION
)

val stkPushRequest = StkPushRequest(
    businessShortCode = "YOUR_SHORTCODE",
    passKey = "YOUR_PASSKEY",
    transactionType = StkPushTransactionType.CustomerPayBillOnline,
    amount = "1",
    phoneNumber = "2547XXXXXXXX",
    partyA = "2547XXXXXXXX",
    partyB = "YOUR_SHORTCODE",
    callBackUrl = "https://your-callback-url.com/mpesa",
    accountReference = "YOUR_ACCOUNT_REFERENCE",
    transactionDesc = "Payment for something",
)

val response = mpesa.initiateStkPush(stkPushRequest)

println("Response: $response")
```
Replace the placeholders with your actual values.

## Contributing

Please feel free
to [open an issue](https://github.com/jeffnyauke/mpesa-sdk-kotlin/issues/new/choose) if you have any
questions or suggestions. Or participate in
the [discussion](https://github.com/jeffnyauke/mpesa-kmp-library/discussions). If you want to
contribute, please read
the [contribution guidelines](https://github.com/jeffnyauke/mpesa-kmp-library/blob/main/CONTRIBUTING.md)
for more information.

## License

**mpesa-sdk-kotlin** is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.

## Trademarks

M-PESA is a trademark of Vodafone Group Plc. and is not affiliated with this project.
