package org.hotchoco.core.register.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class AccountResponse(
    val status: Int,
    val view: String,
    val viewData: JsonElement,
)
