package org.hotchoco.core.register.request

import kotlinx.serialization.Serializable

@Serializable
data class PasscodeRequest(
    val passcode: String
)