package local.sandbox

import com.github.jeffnyauke.mpesa.Mpesa
import com.github.jeffnyauke.mpesa.models.request.AccountBalanceOrganizationType
import com.github.jeffnyauke.mpesa.models.request.AccountBalanceRequest
import com.github.jeffnyauke.mpesa.models.request.B2cCommandId
import com.github.jeffnyauke.mpesa.models.request.B2cRequest
import com.github.jeffnyauke.mpesa.models.request.C2bCommandId
import com.github.jeffnyauke.mpesa.models.request.C2bRegisterRequest
import com.github.jeffnyauke.mpesa.models.request.C2bRegisterTransactionType
import com.github.jeffnyauke.mpesa.models.request.C2bRequest
import com.github.jeffnyauke.mpesa.models.request.DynamicQrRequest
import com.github.jeffnyauke.mpesa.models.request.DynamicQrTransactionType
import com.github.jeffnyauke.mpesa.models.request.StkPushQueryRequest
import com.github.jeffnyauke.mpesa.models.request.StkPushRequest
import com.github.jeffnyauke.mpesa.models.request.StkPushTransactionType
import com.github.jeffnyauke.mpesa.models.request.TransactionReversalReceiverOrganizationType
import com.github.jeffnyauke.mpesa.models.request.TransactionReversalRequest
import com.github.jeffnyauke.mpesa.models.request.TransactionStatusOrganizationType
import com.github.jeffnyauke.mpesa.models.request.TransactionStatusRequest
import com.github.jeffnyauke.mpesa.models.response.AccountBalanceResponse
import kotlinx.coroutines.runBlocking

fun main(){ runBlocking {
  //runCatching {
    val mpesa = Mpesa(
      "pEGbLLYFJUwWZwyXwhc3Gr0GI1ADsHFS", "Eo3OMH48MZNE82vV"
    )

    val dynamicQrResponse = mpesa.generateDynamicQr(
      DynamicQrRequest(
        merchantName = "TEST SUPERMARKET",
        refNo = "Invoice Test",
        amount = "10",
        transactionType = DynamicQrTransactionType.BuyGoods,
        cpi = "373132"
      )
    )
    println(dynamicQrResponse)

    val stkPushResponse = mpesa.initiateStkPush(
      StkPushRequest(
        "174379", "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
        transactionType = StkPushTransactionType.CustomerPayBillOnline,
        amount = "1",
        phoneNumber = "254708374149",
        partyA = "254708374149",
        partyB = "174379",
        callBackUrl = "https://mydomain.com/path",
        accountReference = "CompanyXLTD",
        transactionDesc = "Payment of X",
      )
    )
    println(stkPushResponse)

//    val stkPushQueryResponse = mpesa.stkPushQuery(
//      StkPushQueryRequest(
//        "174379", "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919", stkPushResponse.checkoutRequestId
//      )
//    )
//    println(stkPushQueryResponse)

    val c2bRegisterResponse = mpesa.registerC2bUrls(
      C2bRegisterRequest(
        "600984",
        C2bRegisterTransactionType.Completed,
        "https://mydomain.com/confirmation",
        "https://mydomain.com/validation"
      )
    )
    println(c2bRegisterResponse)

    val c2bResponse = mpesa.initiateC2bTransaction(
      C2bRequest(
        "600984", C2bCommandId.CustomerPayBillOnline, "1", "254708374149", "NEF61H8J60J"
      )
    )
    println(c2bResponse)

    val b2cResponse = mpesa.initiateB2cTransaction(
      B2cRequest(
        "testapi",
        "kucvfgd6BX7F729usOZ2++p3Ytsilk5XNCx+0zc5D3SejR73lDgzaDpRQmYixf51h59XOo6B4isZbpzPguyhQ8ixk54wG8zMY8OQCnLzQoFNgH9j9a66MKDyCRW08H9uRYWCoarwyb4f5eaNQhYX41M4rmrdnNYaQW22sluANIF5R+MMe2AIZ1on2yD0Y9Un7xXoGWxU4vby8SR5nvcuF3RWd1H9AGCRpC1/GeSm0ksCFwq9hc3XX5MFpVQ1v+eL7uPMe1M5bT3qzXDr6mSgzaRR7Aq+ZuBk9+C5FddlBRPqXqhcUFCKLh2Xm59tvfcIMm8/opcH2JcMzIIYMl9dIg==",
        B2cCommandId.SalaryPayment,
        "1",
        "600999",
        "254708374149",
        "https://mydomain.com/b2c/result",
        "https://mydomain.com/b2c/queue",
        "Test Remarks",
        ""
      )
    )
    println(b2cResponse)

    val transactionStatusResponse = mpesa.getTransactionStatus(
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
    )
    println(transactionStatusResponse)

    val accountBalanceResponse = mpesa.getAccountBalance(
      AccountBalanceRequest(
        "testapi",
        "LV4qqmWzMhQ/lJtlNHpkwy/w7RXJa4/ImmVJbXvxgvy0uunc0ZYAgwA0Opyd3sArKtrO9ICzlk09T5LM7EEBJt31WyaPDEKN99fbcmFilhHDJCLfclfB7YF1fC1ELPfeeUP9oQ4RMO5M0jS/VfCSxCzvuY+sUdx6CC+bdBren4k39es034ANjttwuZCiGKCTMztnzGySvwF1bF69ut+hZiBMS7diZ+X/g5UolseoQkXPFOxnjN3/uXVuHuUOYaE/XY6IvK6ewJn4nNt0ZijUwnHytqycdtwMZyZl+xmtmY8pJocVZUBnq6mhvZMpWi71bHxKIVWvJkt+Y42Kul+E7A==",
        "600983",
        AccountBalanceOrganizationType.Shortcode,
        "https://mydomain.com/AccountBalance/result/",
        "https://mydomain.com/AccountBalance/queue/",
        ""
      )
    )
    println(accountBalanceResponse)

    val transactionReversalResponse = mpesa.transactionReversal(
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
    )
    println(transactionReversalResponse)
  }
//}
}
