package org.hotchoco.core.register.viewdata

import kotlinx.serialization.Serializable

@Serializable
data class ProfileViewData(
    val nickname: String = "(알 수 없음)",
    val profileImageUrl: String = "",
    val showOptionalFields: Boolean = false,
    val friendAutomation: Boolean = false,
)
