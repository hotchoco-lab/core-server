package org.hotchoco.core.model

import kotlinx.serialization.Serializable

@Serializable
data class AlertDataModel(
    val title: String? = null,
    val message: String = "",
    val buttons: List<ButtonModel> = listOf()
)
