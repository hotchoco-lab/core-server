package org.hotchoco.core.register.viewdata

import kotlinx.serialization.Serializable
import org.hotchoco.core.register.model.CountriesModel

@Serializable
data class PhoneNumberViewData(
    val countries: CountriesModel = CountriesModel()
)