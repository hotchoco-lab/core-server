package org.hotchoco.core.register.response

import kotlinx.serialization.Serializable
import org.hotchoco.core.register.model.CountryModel

@Serializable
data class TermsResponse(
    val countries: CountriesData = CountriesData()
)

@Serializable
data class CountriesData(
    val all: List<CountryModel> = listOf()
)
