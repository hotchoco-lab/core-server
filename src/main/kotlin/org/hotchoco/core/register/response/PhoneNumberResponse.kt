package org.hotchoco.core.register.response

import kotlinx.serialization.Serializable
import org.hotchoco.core.register.model.PhoneNumberModel

@Serializable
data class PhoneNumberResponse(
    val methods: List<String>,
    val phoneNumber: PhoneNumberModel,
    val retryTime: Int,
    val enenableAccessibilityArs: Boolean
)