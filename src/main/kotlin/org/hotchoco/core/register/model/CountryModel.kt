package org.hotchoco.core.register.model

import kotlinx.serialization.Serializable

@Serializable
data class CountryModel(
    val iso: String,
    val code: String,
    val name: String
)
