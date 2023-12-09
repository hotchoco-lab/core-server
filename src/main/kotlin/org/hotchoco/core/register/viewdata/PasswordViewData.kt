package org.hotchoco.core.register.viewdata

import kotlinx.serialization.Serializable
import org.hotchoco.core.register.model.PhoneNumberModel

@Serializable
data class PasswordViewData(
    val phoneNumber: PhoneNumberModel
)
