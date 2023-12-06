package org.hotchoco.core.register.response

import kotlinx.serialization.Serializable
import org.hotchoco.core.model.AlertDataModel

@Serializable
data class AccountResponse<T>(
    val status: Int,
    val view: String? = null,
    val viewData: T? = null,
    val message: String? = null,
    val alertData: AlertDataModel? = null
)
