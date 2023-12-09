package org.hotchoco.core.register.response

import kotlinx.serialization.Serializable
import org.hotchoco.core.register.model.PhoneNumberModel

@Serializable
data class PasscodeResponse(
    val phoneNumber: PhoneNumberModel
)