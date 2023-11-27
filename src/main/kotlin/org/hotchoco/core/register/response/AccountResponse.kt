package org.hotchoco.core.register.response

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse<T>(
    val status: Int,
    val view: String,
    val viewData: T,
)
