package org.hotchoco.core.register.route

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.hotchoco.core.model.AlertDataModel
import org.hotchoco.core.model.ButtonModel
import org.hotchoco.core.plugins.RegisterSession
import org.hotchoco.core.register.model.CountryModel
import org.hotchoco.core.register.model.RegisterLevel
import org.hotchoco.core.register.model.TermModel
import org.hotchoco.core.register.request.TermsRequest
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.response.CountriesData
import org.hotchoco.core.register.model.TermsListModel
import org.hotchoco.core.register.response.TermsResponse
import java.util.*

fun Route.routeRegister() {
    route("/android/account2") {
        get("/new") {
            call.sessions.set(
                RegisterSession(
                    uuid = UUID.randomUUID().toString(),
                    level = RegisterLevel.NEW
                )
            )

            call.respond(
                AccountResponse(
                    status = 0,
                    view = "terms",
                    viewData = TermsListModel(
                        terms = listTerms(application.environment.config)
                    )
                )
            )
        }

        post<TermsRequest>("/terms") {
            val terms = listTerms(application.environment.config)

            val essentialTerms = terms.filter { it.essential }

            if (essentialTerms.any { term -> term.code !in it.codes }) {
                return@post call.respond(
                    AccountResponse<Unit>(
                        status = 0,
                        message = "필수 약관 미동의시 서비스 이용이 불가합니다. 필수 약관을 확인하고 동의해 주세요.",
                        alertData = AlertDataModel(
                            title = null,
                            message = "필수 약관 미동의시 서비스 이용이 불가합니다. 필수 약관을 확인하고 동의해 주세요.",
                            buttons = listOf(
                                ButtonModel(
                                    name = "확인",
                                    view = "terms",
                                    viewData = DefaultJson.encodeToJsonElement(
                                        TermsListModel.serializer(),
                                        TermsListModel(
                                            terms = terms
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            }

            val session = call.sessions.get<RegisterSession>()
                ?: return@post call.respond(
                    AccountResponse<Unit>(
                        status = 0,
                        message = "일시적인 오류로 처음으로 돌아갑니다. 다시 시도해 주세요.",
                        alertData = AlertDataModel(
                            title = null,
                            message = "일시적인 오류로 처음으로 돌아갑니다. 다시 시도해 주세요.",
                            buttons = listOf(
                                ButtonModel(
                                    name = "확인",
                                    view = "login"
                                )
                            )
                        )
                    )
                )

            call.respond(
                AccountResponse(
                    status = 0,
                    view = "phone-number",
                    viewData = TermsResponse(
                        countries = CountriesData.create(
                            listOf(
                                CountryModel(
                                    iso = "KR",
                                    code = "82",
                                    name = "대한민국"
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun listTerms(config: ApplicationConfig): List<TermModel> {
    val stream = TermModel::class.java.classLoader.getResourceAsStream(
        config.property("hotchoco.register.termsJsonPath").getString()
    )
        ?: throw RuntimeException("terms is null")

    return Json.decodeFromStream<List<TermModel>>(stream)
}