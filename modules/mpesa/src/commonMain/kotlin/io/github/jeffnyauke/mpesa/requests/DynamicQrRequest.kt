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

package io.github.jeffnyauke.mpesa.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Dynamic QR request
 *
 * @param merchantName Name of the Company/M-Pesa Merchant Name. Sample: Safaricom LTD
 * @param refNo Transaction Reference. Sample: rf38f04
 * @param amount The total amount for the sale/transaction. Sample: 20000
 * @param trxCode Transaction Type. The supported types are: BG: Pay Merchant (Buy Goods). WA:
 *     Withdraw Cash at Agent Till. PB: Paybill or Business number. SM: Send Money(Mobile number)
 *     SB: Sent to Business. Business number CPI in MSISDN format.
 * @param cpi Credit Party Identifier. Can be a Mobile Number, Business Number, Agent Till, PayBill
 *     or Business number, or Merchant Buy Goods. Sample: 17408
 */
@Serializable
public data class DynamicQrRequest internal constructor(
    @SerialName("MerchantName") val merchantName: String,
    @SerialName("RefNo") val refNo: String,
    @SerialName("Amount") val amount: String,
    @SerialName("TrxCode") val trxCode: String,
    @SerialName("CPI") val cpi: String,
) {
    /**
     * @param merchantName Name of the Company/M-Pesa Merchant Name. Sample: Safaricom LTD
     * @param refNo Transaction Reference. Sample: rf38f04
     * @param amount The total amount for the sale/transaction. Sample: 20000
     * @param transactionType [DynamicQrTransactionType] Transaction Type. Sample:
     *     [DynamicQrTransactionType.BuyGoods]
     * @param cpi Credit Party Identifier. Can be a Mobile Number, Business Number, Agent Till,
     *     PayBill or Business number, or Merchant Buy Goods. Sample: 17408
     */
    public constructor(
        merchantName: String,
        refNo: String,
        amount: String,
        transactionType: DynamicQrTransactionType,
        cpi: String,
    ) : this(merchantName, refNo, amount, transactionType.code, cpi)
}
