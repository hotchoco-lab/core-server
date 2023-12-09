package org.hotchoco.core.register.model

import kotlinx.serialization.Serializable

@Serializable
data class CountriesModel(
    val all: List<CountryModel> = listOf(),
    val recommended: List<CountryModel> = listOf()
) {
    companion object {
        fun create(countries: List<CountryModel>): CountriesModel {
            return CountriesModel(
                all = countries,
                recommended = countries
            )
        }
    }
}
