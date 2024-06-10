![M-Pesa SDK Kotlin Banner](/docs/Writerside/images/mpesa_sdk_kotlin_white.png)
[![Build](https://github.com/jeffnyauke/mpesa-sdk-kotlin/actions/workflows/check.yml/badge.svg)](https://github.com/jeffnyauke/mpesa-kmp-library/actions/workflows/check.yml)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.22-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jeffnyauke/mpesa-sdk-kotlin?color=blue)](https://search.maven.org/search?q=g:io.github.jeffnyauke.mpesa)

## Introduction

This SDK provides a convenient way to interact with the Safaricom **M-Pesa Daraja API v2.0** from your projects.

**Features**

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
* Built with **Kotlin Multiplatform**, allowing you to use the same codebase across different platforms.
* Provides a **simple** and **intuitive** API surface.
* Handles **authentication** and **token management**.

## Documentation

For detailed documentation, API reference, and usage examples, please visit the [official website](https://jeffnyauke.github.io/mpesa-sdk-kotlin/).

## Installation

```kotlin
dependencies {
    implementation("io.github.jeffnyauke:mpesa:x.y.z")
}
```

## At a Glance

```kotlin
val mpesa = Mpesa(
    consumerKey = "YOUR_CONSUMER_KEY",
    consumerSecret = "YOUR_CONSUMER_SECRET",
    environment = Environment.SANDBOX
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

> [!NOTE]
> Replace the placeholders with your actual values.

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
