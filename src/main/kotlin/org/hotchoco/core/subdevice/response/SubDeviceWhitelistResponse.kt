package org.hotchoco.core.subdevice.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubDeviceWhitelistResponse(
    @SerialName("allowlisted")
    val allowListed: Boolean,
)
