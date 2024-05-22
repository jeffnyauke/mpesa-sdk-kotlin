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

package com.github.jeffnyauke.mpesa.sandbox

import com.github.jeffnyauke.mpesa.Mpesa
import com.github.jeffnyauke.mpesa.request.AccountBalanceOrganizationType
import com.github.jeffnyauke.mpesa.request.AccountBalanceRequest
import com.github.jeffnyauke.mpesa.request.B2cCommandId
import com.github.jeffnyauke.mpesa.request.B2cRequest
import com.github.jeffnyauke.mpesa.request.C2bCommandId
import com.github.jeffnyauke.mpesa.request.C2bRegisterRequest
import com.github.jeffnyauke.mpesa.request.C2bRegisterTransactionType
import com.github.jeffnyauke.mpesa.request.C2bRequest
import com.github.jeffnyauke.mpesa.request.DynamicQrRequest
import com.github.jeffnyauke.mpesa.request.DynamicQrTransactionType
import com.github.jeffnyauke.mpesa.request.StkPushQueryRequest
import com.github.jeffnyauke.mpesa.request.StkPushRequest
import com.github.jeffnyauke.mpesa.request.StkPushTransactionType
import com.github.jeffnyauke.mpesa.request.TransactionReversalReceiverOrganizationType
import com.github.jeffnyauke.mpesa.request.TransactionReversalRequest
import com.github.jeffnyauke.mpesa.request.TransactionStatusOrganizationType
import com.github.jeffnyauke.mpesa.request.TransactionStatusRequest
import com.github.jeffnyauke.mpesa.response.StkPushResponse
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        runCatching {
            val mpesa = Mpesa(
                "pEGbLLYFJUwWZwyXwhc3Gr0GI1ADsHFS",
                "Eo3OMH48MZNE82vV"
            )
            val stkPushResponse = initiateStkPush(mpesa)
            queryStkPushStatus(mpesa, stkPushResponse)
            registerC2bUrls(mpesa)
            initiateC2bTransaction(mpesa)
            initiateB2cTransaction(mpesa)
            getTransactionStatus(mpesa)
            getAccountBalance(mpesa)
            transactionReversal(mpesa)
            generateDynamicQr(mpesa)
        }
    }
}

private suspend fun transactionReversal(mpesa: Mpesa) {
    mpesa.transactionReversal(
        TransactionReversalRequest(
            "testapi",
            "GonFoZ/NAvkwxXHx7VZz0AqzP0TjyaXv2g3uNWt1NLy275MKBEOwOFUFHuHVbQOpvV" +
                "6teJ9r6uO1L0D2f10S5Fx08Dcc2vXXjxOieuOgzvqJAXjiuFr4BlIFKbNHbzZkTxjkCQHziigQtUBVa" +
                "Q5YOdXncDMyhoukpuK/u3288Xbz7HGW/HB6VRYSXD6QU17+0PLOHNh2xnejxiraeRhXJD/J4r9ZsTGgl" +
                "5tg3HOddcqmpFEZOgMMYu1dpRyJ8FyBoJpwthfBPN/3mnef6px1Xi5aYRmLxTyWWec7FENqnYZ5b/X5b" +
                "jjMelnd/A3QuNIiIweajbEVHItyTfRw7T3S7Q==",
            "OEI2AK4Q16",
            "1",
            "600980",
            TransactionReversalReceiverOrganizationType.Default,
            "https://mydomain.com/Reversal/result/",
            "https://mydomain.com/Reversal/queue/",
            "Test Remarks",
            ""
        )
    ).also {
        println(it)
    }
}

private suspend fun getAccountBalance(mpesa: Mpesa) {
    mpesa.getAccountBalance(
        AccountBalanceRequest(
            "testapi",
            "LV4qqmWzMhQ/lJtlNHpkwy/w7RXJa4/ImmVJbXvxgvy0uunc0ZYAgwA0Opyd3sArKtrO" +
                "9ICzlk09T5LM7EEBJt31WyaPDEKN99fbcmFilhHDJCLfclfB7YF1fC1ELPfeeUP9oQ4RMO5M0jS/VfC" +
                "SxCzvuY+sUdx6CC+bdBren4k39es034ANjttwuZCiGKCTMztnzGySvwF1bF69ut+hZiBMS7diZ+X/g5U" +
                "olseoQkXPFOxnjN3/uXVuHuUOYaE/XY6IvK6ewJn4nNt0ZijUwnHytqycdtwMZyZl+xmtmY8pJocVZUB" +
                "nq6mhvZMpWi71bHxKIVWvJkt+Y42Kul+E7A==",
            "600983",
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
            "Qmo18nyB0MMMFWewXj6C2WfJfwMaRfPxW0mF9MEhPMr+p3ggopVuzHOkue8fG9QHdnwf" +
                "saudadGaKSPBsTHEKITO+awdz9c2YV+v6vAuumOWFEgNZE8Su9tXRFVNr9dPceU2DAx8e07/uo7pc6l" +
                "h/DXJOse6ihRT6DHpEuoWlF74QShjnK3nYb6Ta4X09QNOPlwdgSM4wNC6FhIXgqITtDXG6Il8bpQ8yi" +
                "2akI6CwKQEIEIsINreJ0ZoTvaDB7VRNB4pJ1UXGq3KZk5jZ/mORMNQidXIFH9uIxUYsGtm9kLTMgzKr" +
                "SN8bMJZ7tp2i5SGjyOTCQaFxF3oE/MKFzj0hA==",
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
            "testapi",
            "kucvfgd6BX7F729usOZ2++p3Ytsilk5XNCx+0zc5D3SejR73lDgzaDpRQmYixf51h59" +
                "XOo6B4isZbpzPguyhQ8ixk54wG8zMY8OQCnLzQoFNgH9j9a66MKDyCRW08H9uRYWCoarwyb4f5eaNQ" +
                "hYX41M4rmrdnNYaQW22sluANIF5R+MMe2AIZ1on2yD0Y9Un7xXoGWxU4vby8SR5nvcuF3RWd1H9AGC" +
                "RpC1/GeSm0ksCFwq9hc3XX5MFpVQ1v+eL7uPMe1M5bT3qzXDr6mSgzaRR7Aq+ZuBk9+C5FddlBRPqXq" +
                "hcUFCKLh2Xm59tvfcIMm8/opcH2JcMzIIYMl9dIg==",
            B2cCommandId.SalaryPayment,
            "1",
            "600999",
            "254708374149",
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
