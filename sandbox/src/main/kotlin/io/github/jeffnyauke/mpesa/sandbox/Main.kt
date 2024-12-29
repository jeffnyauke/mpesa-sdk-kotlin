/*
 * Copyright (c) 2024 Jeffrey Nyauke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jeffnyauke.mpesa.samples.jvm

import io.github.jeffnyauke.mpesa.Mpesa
import io.github.jeffnyauke.mpesa.requests.AccountBalanceOrganizationType
import io.github.jeffnyauke.mpesa.requests.AccountBalanceRequest
import io.github.jeffnyauke.mpesa.requests.B2cCommandId
import io.github.jeffnyauke.mpesa.requests.B2cRequest
import io.github.jeffnyauke.mpesa.requests.C2bCommandId
import io.github.jeffnyauke.mpesa.requests.C2bRegisterRequest
import io.github.jeffnyauke.mpesa.requests.C2bRegisterTransactionType
import io.github.jeffnyauke.mpesa.requests.C2bRequest
import io.github.jeffnyauke.mpesa.requests.DynamicQrRequest
import io.github.jeffnyauke.mpesa.requests.DynamicQrTransactionType
import io.github.jeffnyauke.mpesa.requests.StkPushQueryRequest
import io.github.jeffnyauke.mpesa.requests.StkPushRequest
import io.github.jeffnyauke.mpesa.requests.StkPushTransactionType
import io.github.jeffnyauke.mpesa.requests.TransactionReversalReceiverOrganizationType
import io.github.jeffnyauke.mpesa.requests.TransactionReversalRequest
import io.github.jeffnyauke.mpesa.requests.TransactionStatusOrganizationType
import io.github.jeffnyauke.mpesa.requests.TransactionStatusRequest
import io.github.jeffnyauke.mpesa.responses.StkPushResponse
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        runCatching {
            val mpesa = Mpesa(
                "CuM0KGFzIwcqCJS8Eu4VITrI2cp5N2Cp",
                "XW5J2RGmnFrL7yKP"
            )
            // val stkPushResponse = initiateStkPush(mpesa)
            // queryStkPushStatus(mpesa, stkPushResponse)
            // registerC2bUrls(mpesa)
            // initiateC2bTransaction(mpesa)
            // initiateB2cTransaction(mpesa)
            // getTransactionStatus(mpesa)
            // getAccountBalance(mpesa)
            // transactionReversal(mpesa)
            // generateDynamicQr(mpesa)
        }
    }
}

private suspend fun transactionReversal(mpesa: Mpesa) {
    mpesa.transactionReversal(
        TransactionReversalRequest(
            "testapi",
            "GonFoZ/NAvkwxXHx7VZz0AqzP0TjyaXv2g3uNWt1NLy275MKBEOwOFUFHuHVbQOpvV6teJ9r6uO1L0D2f10S5Fx08Dcc2vXXjxOieuOgzvqJAXjiuFr4BlIFKbNHbzZkTxjkCQHziigQtUBVaQ5YOdXncDMyhoukpuK/u3288Xbz7HGW/HB6VRYSXD6QU17+0PLOHNh2xnejxiraeRhXJD/J4r9ZsTGgl5tg3HOddcqmpFEZOgMMYu1dpRyJ8FyBoJpwthfBPN/3mnef6px1Xi5aYRmLxTyWWec7FENqnYZ5b/X5bjjMelnd/A3QuNIiIweajbEVHItyTfRw7T3S7Q==",
            "OEI2AK4Q16",
            "1",
            "600980",
            TransactionReversalReceiverOrganizationType.Default,
            "https://mydomain.com/Reversal/result/",
            "https://mydomain.com/Reversal/queue/",
            "Test Remarks",
            ""
        )
    ).also(::println)
}

private suspend fun getAccountBalance(mpesa: Mpesa) {
    mpesa.getAccountBalance(
        AccountBalanceRequest(
            "weza",
            "ipnaagxLiFq6gXOfC13sI9s11bXgF+1BNCsGKnwJsq8HuJ6WbhyVk8LbJ8tsutgBO6onYb6L1zlvpVftvsFZr/ul7Lnr6GRhjSAN/Gnrd/y1Wa3aaLCfDhQF1Rj2hcagR3/+7Bb2S8vXFHFPQ647lQPGUIjmYD/n7IGMBnoNBLZOGx6NK02d4iv4NBDT8BvLXetm6FGaazAPp86dosOfa+jpuRkK715vfIg2n5klhzrWHAPzi4zgEi39lrB4zwjFUos7e76A6OaklE4+w5a0l4K+yI5ZVsTHGiY5Q/YfXis95kFlZkxSk5f8n+ehAcYX30mXklPAifEM0k2j1deOOQ==",
            "794339",
            AccountBalanceOrganizationType.Shortcode,
            "https://mydomain.com/AccountBalance/result/",
            "https://mydomain.com/AccountBalance/queue/",
            ""
        )
    ).also {
        println(it)
    }
}

private suspend fun getTransactionStatus(mpesa: Mpesa) {
    mpesa.getTransactionStatus(
        TransactionStatusRequest(
            "testapi",
            "Qmo18nyB0MMMFWewXj6C2WfJfwMaRfPxW0mF9MEhPMr+p3ggopVuzHOkue8fG9QHdnwfsaudadGaKSPBsTHEKITO+awdz9c2YV+v6vAuumOWFEgNZE8Su9tXRFVNr9dPceU2DAx8e07/uo7pc6lh/DXJOse6ihRT6DHpEuoWlF74QShjnK3nYb6Ta4X09QNOPlwdgSM4wNC6FhIXgqITtDXG6Il8bpQ8yi2akI6CwKQEIEIsINreJ0ZoTvaDB7VRNB4pJ1UXGq3KZk5jZ/mORMNQidXIFH9uIxUYsGtm9kLTMgzKrSN8bMJZ7tp2i5SGjyOTCQaFxF3oE/MKFzj0hA==",
            "NEF61H8J60",
            "AG_20190826_0000777ab7d848b9e721",
            "600999",
            TransactionStatusOrganizationType.Shortcode,
            "https://mydomain.com/TransactionStatus/queue/",
            "https://mydomain.com/TransactionStatus/result/",
            "Test Remarks",
            ""
        )
    ).also {
        println(it)
    }
}

private suspend fun initiateB2cTransaction(mpesa: Mpesa) {
    mpesa.initiateB2cTransaction(
        B2cRequest(
            "weza",
            "ipnaagxLiFq6gXOfC13sI9s11bXgF+1BNCsGKnwJsq8HuJ6WbhyVk8LbJ8tsutgBO6onYb6L1zlvpVftvsFZr/ul7Lnr6GRhjSAN/Gnrd/y1Wa3aaLCfDhQF1Rj2hcagR3/+7Bb2S8vXFHFPQ647lQPGUIjmYD/n7IGMBnoNBLZOGx6NK02d4iv4NBDT8BvLXetm6FGaazAPp86dosOfa+jpuRkK715vfIg2n5klhzrWHAPzi4zgEi39lrB4zwjFUos7e76A6OaklE4+w5a0l4K+yI5ZVsTHGiY5Q/YfXis95kFlZkxSk5f8n+ehAcYX30mXklPAifEM0k2j1deOOQ==",
            B2cCommandId.BusinessPayment,
            "10",
            "794339",
            "254715847493",
            "https://mydomain.com/b2c/result",
            "https://mydomain.com/b2c/queue",
            "Test Remarks",
            ""
        )
    ).also {
        println(it)
    }
}

private suspend fun initiateC2bTransaction(mpesa: Mpesa) {
    mpesa.initiateC2bTransaction(
        C2bRequest(
            "600984",
            C2bCommandId.CustomerPayBillOnline,
            "1",
            "254708374149",
            "NEF61H8J60J"
        )
    ).also {
        println(it)
    }
}

private suspend fun registerC2bUrls(mpesa: Mpesa) {
    mpesa.registerC2bUrls(
        C2bRegisterRequest(
            "600984",
            C2bRegisterTransactionType.Completed,
            "https://mydomain.com/confirmation",
            "https://mydomain.com/validation"
        )
    ).also {
        println(it)
    }
}

private suspend fun queryStkPushStatus(
    mpesa: Mpesa,
    stkPushResponse: StkPushResponse
) {
    mpesa.queryStkPushStatus(
        StkPushQueryRequest(
            "174379",
            "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
            stkPushResponse.checkoutRequestId
        )
    ).also {
        println(it)
    }
}

private suspend fun initiateStkPush(mpesa: Mpesa): StkPushResponse {
    return mpesa.initiateStkPush(
        StkPushRequest(
            "174379",
            "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
            transactionType = StkPushTransactionType.CustomerPayBillOnline,
            amount = "1",
            phoneNumber = "254708374149",
            partyA = "254708374149",
            partyB = "174379",
            callBackUrl = "https://mydomain.com/path",
            accountReference = "CompanyXLTD",
            transactionDesc = "Payment of X",
        )
    ).also {
        println(it)
    }
}

private suspend fun generateDynamicQr(mpesa: Mpesa) {
    mpesa.generateDynamicQr(
        DynamicQrRequest(
            merchantName = "TEST SUPERMARKET",
            refNo = "Invoice Test",
            amount = "10",
            transactionType = DynamicQrTransactionType.BuyGoods,
            cpi = "373132"
        )
    ).also {
        println(it)
    }
}
