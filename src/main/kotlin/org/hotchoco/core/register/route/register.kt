package org.hotchoco.core.register.route

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromStream
import org.hotchoco.core.helper.CoreHelper
import org.hotchoco.core.plugins.RegisterSession
import org.hotchoco.core.register.model.RegisterStage
import org.hotchoco.core.register.model.TermModel
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.response.NewResponse
import java.util.UUID

fun Route.routeRegister() {
    val registerSessionStorage = CoreHelper.registerSessionStorage

    route("/android/account2") {
        get("/new") {
            call.response.apply {
                header(
                    "C",
                    UUID.randomUUID().toString()
                )

                header("Kakao", "Talk")
            }

            call.sessions.set(
                "Set-SS",
                RegisterSession(
                    uuid = UUID.randomUUID().toString(),
                    stage = RegisterStage.NEW
                )
            )

            call.respond(
                AccountResponse(
                    status = 0,
                    view = "terms",
                    viewData = NewResponse(
                        terms = listTerms(application.environment.config)
                    )
                )
            )
        }

        post("/terms") {
            val session = call.sessions.get<RegisterSession>()

            call.respond(
                session ?: buildJsonObject {  }
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun listTerms(config: ApplicationConfig): List<TermModel> {
    val stream = TermModel::class.java.classLoader.getResourceAsStream(config.property("hotchoco.register.termsJsonPath").getString())
        ?: throw RuntimeException("terms is null")

    return Json.decodeFromStream<List<TermModel>>(stream)
}