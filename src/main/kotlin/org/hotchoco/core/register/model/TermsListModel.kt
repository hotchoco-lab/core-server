package org.hotchoco.core.register.model

import kotlinx.serialization.Serializable

@Serializable
data class TermsListModel(
    val terms: List<TermModel>
)
