package org.hotchoco.core.register.model

import kotlinx.serialization.Serializable

@Serializable
data class TermModel(
    val title: String,
    val description: String,
    val code: String,
    val essential: Boolean
)
