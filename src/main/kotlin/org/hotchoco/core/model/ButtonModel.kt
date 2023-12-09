package org.hotchoco.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject

@Serializable
data class ButtonModel(
    val name: String,
    val view: String,
    val viewData: JsonElement = buildJsonObject { },
)