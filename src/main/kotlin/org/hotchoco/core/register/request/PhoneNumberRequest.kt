package org.hotchoco.core.register.request

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumberRequest(
    val countryCode: String,
    val countryIso: String,
    val phoneNumber: String,
    val method: String,
    val termCodes: List<String>,
    val simPhoneNumber: String
)