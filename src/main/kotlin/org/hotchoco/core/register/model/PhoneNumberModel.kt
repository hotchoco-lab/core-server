package org.hotchoco.core.register.model

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumberModel(
    val countryIso: String,
    val countryCode: String,
    val pstnNumber: String,
    val beautifiedPstnNumber: String,
    val nsnNumber: String,
    val beautifiedNsnNumber: String
)