/*
 * Copyright 2023 Jeffrey Nyauke
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

package com.github.jeffnyauke.mpesa.models.request

import com.github.jeffnyauke.mpesa.util.encodeBase64
import com.github.jeffnyauke.mpesa.util.getDarajaTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stk push request
 *
 * @param businessShortCode This is organizations shortcode (PayBill or BuyGoods - A 5 to 6-digit account number) used to identify an organization and receive the transaction. Sample: Shortcode (5 to 7 digits) e.g. 654321.
 * @param password This is the password used for encrypting the request sent: A base64 encoded string. (The base64 string is a combination of Shortcode+Passkey+Timestamp).
 * @param timestamp This is the Timestamp of the transaction, normally in the format of YEAR+MONTH+DATE+HOUR+MINUTE+SECOND (YYYYMMDDHHMMSS) Each part should be at least two digits apart from the year which takes four digits.
 * @param transactionType This is the transaction type that is used to identify the transaction when sending the request to M-PESA.
 * @param amount This is the Amount transacted normally a numeric value. Money that customer pays to the Shortcode. Only whole numbers are supported. Sample: 10.
 * @param phoneNumber The Mobile Number to receive the STK Pin Prompt. This number can be the same as PartyA value above. MSISDN (12 digits Mobile Number). Sample: MSISDN (12 digits Mobile Number) e.g. 254XXXXXXXXX.
 * @param partyA The phone number sending money. The parameter expected is a Valid Safaricom Mobile Number that is M-PESA registered in the format 254XXXXXXXXX. Sample: MSISDN (12 digits Mobile Number) e.g. 254XXXXXXXXX.
 * @param partyB The organization receiving the funds. The parameter expected is a 5 to 6 digit as defined on the Shortcode description above. This can be the same as BusinessShortCode value above. Sample: Shortcode (5-7 digits).
 * @param callBackUrl A CallBack URL is a valid secure URL that is used to receive notifications from M-Pesa API. It is the endpoint to which the results will be sent by M-Pesa API. Sample: https://ip or domain:port/path, e.g: https://mydomain.com/path, https://0.0.0.0:9090/path.
 * @param accountReference This is an Alpha-Numeric parameter that is defined by your system as an Identifier of the transaction for CustomerPayBillOnline transaction type. Along with the business name, this value is also displayed to the customer in the STK Pin Prompt message. Maximum of 12 characters. Sample: Any combinations of letters and numbers.
 * @param transactionDesc This is any additional information/comment that can be sent along with the request from your system. Maximum of 13 Characters. Sample: Any string between 1 and 13 characters.
 */
@Serializable
public data class StkPushRequest(
  @SerialName("BusinessShortCode") val businessShortCode: String,
  @SerialName("Password") val password: String,
  @SerialName("Timestamp") val timestamp: String,
  @SerialName("TransactionType") val transactionType: String,
  @SerialName("Amount") val amount: String,
  @SerialName("PhoneNumber") val phoneNumber: String,
  @SerialName("PartyA") val partyA: String,
  @SerialName("PartyB") val partyB: String,
  @SerialName("CallBackURL") val callBackUrl: String,
  @SerialName("AccountReference") val accountReference: String,
  @SerialName("TransactionDesc") val transactionDesc: String,
) {
  /**
   * @param businessShortCode This is organizations shortcode (PayBill or BuyGoods - A 5 to 6-digit account number) used to identify an organization and receive the transaction. Sample: Shortcode (5 to 7 digits) e.g. 654321.
   * @param passKey If you need a passkey for mpesa online payments solutions (specifically express stk push) simply email m-pesabusiness@safaricom.co.ke and they will send it to you.
   * @param transactionType This is the transaction type that is used to identify the transaction when sending the request to M-PESA. Sample: [StkPushTransactionType.CustomerBuyGoodsOnline]
   * @param amount This is the Amount transacted normally a numeric value. Money that customer pays to the Shortcode. Only whole numbers are supported. Sample: 10.
   * @param phoneNumber The Mobile Number to receive the STK Pin Prompt. This number can be the same as PartyA value above. MSISDN (12 digits Mobile Number). Sample: MSISDN (12 digits Mobile Number) e.g. 254XXXXXXXXX.
   * @param partyA The phone number sending money. The parameter expected is a Valid Safaricom Mobile Number that is M-PESA registered in the format 254XXXXXXXXX. Sample: MSISDN (12 digits Mobile Number) e.g. 254XXXXXXXXX.
   * @param partyB The organization receiving the funds. The parameter expected is a 5 to 6 digit as defined on the Shortcode description above. This can be the same as BusinessShortCode value above. Sample: Shortcode (5-7 digits).
   * @param callBackUrl A CallBack URL is a valid secure URL that is used to receive notifications from M-Pesa API. It is the endpoint to which the results will be sent by M-Pesa API. Sample: https://ip or domain:port/path, e.g: https://mydomain.com/path, https://0.0.0.0:9090/path.
   * @param accountReference This is an Alpha-Numeric parameter that is defined by your system as an Identifier of the transaction for CustomerPayBillOnline transaction type. Along with the business name, this value is also displayed to the customer in the STK Pin Prompt message. Maximum of 12 characters. Sample: Any combinations of letters and numbers.
   * @param transactionDesc This is any additional information/comment that can be sent along with the request from your system. Maximum of 13 Characters. Sample: Any string between 1 and 13 characters.
   */
  public constructor(
      businessShortCode: String,
      passKey: String,
      timestamp: String = getDarajaTimestamp(),
      transactionType: StkPushTransactionType,
      amount: String,
      phoneNumber: String,
      partyA: String,
      partyB: String,
      callBackUrl: String,
      accountReference: String,
      transactionDesc: String,
  ) : this(
    businessShortCode,
    "$businessShortCode$passKey$timestamp".encodeBase64(),
    timestamp,
    transactionType.name,
    amount,
    phoneNumber,
    partyA,
    partyB,
    callBackUrl,
    accountReference,
    transactionDesc,
  )
}
