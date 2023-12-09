package org.hotchoco.core.register.util

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import org.hotchoco.core.register.model.TermModel

@OptIn(ExperimentalSerializationApi::class)
fun listTerms(config: ApplicationConfig): List<TermModel> {
    val stream = TermModel::class.java.classLoader.getResourceAsStream(
        config.property("hotchoco.register.termsJsonPath").getString()
    )
            ?: throw RuntimeException("terms is null")

    return DefaultJson.decodeFromStream<List<TermModel>>(stream)
}