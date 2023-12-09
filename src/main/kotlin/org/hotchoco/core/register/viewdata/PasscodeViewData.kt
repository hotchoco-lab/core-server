package org.hotchoco.core.register.viewdata

import kotlinx.serialization.Serializable
import org.hotchoco.core.register.model.PhoneNumberModel

@Serializable
data class PasscodeViewData(
    val methods: List<String>,
    val phoneNumber: PhoneNumberModel,
    val retryTime: Int,
    val enenableAccessibilityArs: Boolean
)