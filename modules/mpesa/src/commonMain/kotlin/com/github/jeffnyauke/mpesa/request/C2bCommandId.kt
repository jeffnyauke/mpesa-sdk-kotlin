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

package com.github.jeffnyauke.mpesa.request

/**
 * This is the transaction type that is used to identify the transaction when sending the request to
 * M-PESA.
 */
public enum class C2bCommandId {
    /** Customer pay bill online. */
    CustomerPayBillOnline,

    /** Customer buy goods online. */
    CustomerBuyGoodsOnline,
}
