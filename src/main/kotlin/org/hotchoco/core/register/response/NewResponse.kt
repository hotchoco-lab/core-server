package org.hotchoco.core.register.response

import kotlinx.serialization.Serializable
import org.hotchoco.core.register.model.TermModel

@Serializable
data class NewResponse(
    val terms: List<TermModel>
)
