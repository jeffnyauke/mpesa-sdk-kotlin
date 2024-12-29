# Using the SDK

This section will guide you through initializing the M-Pesa SDK Kotlin, handling authentication, and utilizing the various API endpoints to integrate M-Pesa functionalities into your application.

## Initialization

To begin using the SDK, create an instance of the Mpesa class:

```kotlin
val mpesa = Mpesa(
   consumerKey = "YOUR_CONSUMER_KEY",
   consumerSecret = "YOUR_CONSUMER_SECRET",
   environment = Environment.SANDBOX // Defaults to [Environment.PRODUCTION]
)
```

> **Note**
>
> You can obtain the credentials from [https://developer.safaricom.co.ke/](https://developer.safaricom.co.ke/)
>
{style="note"}
