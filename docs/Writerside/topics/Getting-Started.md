# Getting Started

The M-Pesa SDK Kotlin is a comprehensive toolkit designed to simplify the integration of M-Pesa payment functionalities
into your applications. Developed with the principles of cross-platform compatibility and
developer-friendliness, this SDK enables you to interact seamlessly with the Safaricom M-Pesa Daraja API v2.0 across a
variety of platforms.

## Features

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

## Installation

Add the M-Pesa SDK Kotlin as a dependency to your project using one of the following methods:

<tabs>
    <tab title="Gradle (Kotlin DSL)">
        <code-block lang="kotlin">
            dependencies {
                implementation("io.github.jeffnyauke:mpesa:0.1.0") // Replace with the latest version
            }
        </code-block>
    </tab>
    <tab title="Gradle (Groovy)">
        <code-block lang="groovy">
            dependencies {
                implementation 'io.github.jeffnyauke:mpesa:0.1.0' // Replace with the latest version
            }
        </code-block>
    </tab>
    <tab title="Maven">
        <code-block lang="xml">
            <![CDATA[
                <dependency>
                    <groupId>io.github.jeffnyauke</groupId>
                    <artifactId>mpesa</artifactId>
                    <version>0.1.0</version>  // Replace with the latest version
                </dependency>]]>
        </code-block>
    </tab>
</tabs>
