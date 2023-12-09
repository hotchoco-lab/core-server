package org.hotchoco.core.register.request

import kotlinx.serialization.Serializable

@Serializable
data class TermsRequest(
    val codes: List<String>
)
