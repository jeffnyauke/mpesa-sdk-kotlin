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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * C2b register request
 *
 * @property shortCode Usually, a unique number is tagged to an M-PESA pay bill/till number of the organization. Sample: 123456
 * @property responseType This parameter specifies what is to happen if for any reason the validation URL is not reachable. Note that, this is the default action value that determines what M-PESA will do in the scenario that your endpoint is unreachable or is unable to respond on time.
 * @property confirmationUrl This is the URL that receives the confirmation request from API upon payment completion.
 * @property validationUrl This is the URL that receives the validation request from the API upon payment submission. The validation URL is only called if the external validation on the registered shortcode is enabled. (By default External Validation is disabled).
 */
@Serializable
public data class C2bRegisterRequest(
  @SerialName("ShortCode") val shortCode: String,
  @SerialName("ResponseType") val responseType: String,
  @SerialName("ConfirmationUrl") val confirmationUrl: String,
  @SerialName("ValidationUrl") val validationUrl: String
) {
  /**
   * @property shortCode Usually, a unique number is tagged to an M-PESA pay bill/till number of the organization. Sample: 123456
   * @property responseType This parameter specifies what is to happen if for any reason the validation URL is not reachable. Note that, this is the default action value that determines what M-PESA will do in the scenario that your endpoint is unreachable or is unable to respond on time. Sample : [C2bRegisterTransactionType.Completed].
   * @property confirmationUrl This is the URL that receives the confirmation request from API upon payment completion.
   * @property validationUrl This is the URL that receives the validation request from the API upon payment submission. The validation URL is only called if the external validation on the registered shortcode is enabled. (By default External Validation is disabled).
   */
  public constructor(
      shortCode: String,
      c2bRegisterTransactionType: C2bRegisterTransactionType,
      confirmationUrl: String,
      validationUrl: String,
  ) : this(shortCode, c2bRegisterTransactionType.name, confirmationUrl, validationUrl)
}
