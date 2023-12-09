package org.hotchoco.core.register.viewdata

import kotlinx.serialization.Serializable
import org.hotchoco.core.register.model.TermModel

@Serializable
data class TermsViewData(
    val terms: List<TermModel>
)
