package org.hotchoco.core.register.route

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.hotchoco.core.helper.CoreHelper
import org.hotchoco.core.model.AlertDataModel
import org.hotchoco.core.model.ButtonModel
import org.hotchoco.core.register.model.CountryModel
import org.hotchoco.core.register.model.RegisterLevel
import org.hotchoco.core.register.model.RegisterSession
import org.hotchoco.core.register.viewdata.TermsViewData
import org.hotchoco.core.register.request.TermsRequest
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.model.CountriesModel
import org.hotchoco.core.register.util.fallbackRegister
import org.hotchoco.core.register.util.listTerms
import org.hotchoco.core.register.viewdata.PhoneNumberViewData

internal fun Route.routeRegisterTerms() {
    val registerLevelStorage = CoreHelper.registerLevelStorageGetter!!.invoke()
    
    route("/android/account2") {
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
                                        TermsViewData.serializer(),
                                        TermsViewData(
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
                    ?: return@post call.respond(fallbackRegister())
            
            try {
                val currentLevel = registerLevelStorage.read(session.uuid)

                if (currentLevel != RegisterLevel.NEW.toString()) return@post call.respond(fallbackRegister())
            } catch (e: Throwable) {
                return@post call.respond(fallbackRegister())
            }
            
            registerLevelStorage.write(session.uuid, RegisterLevel.TERMS.toString())
            
            call.respond(
                message = AccountResponse(
                    status = 0,
                    view = "phone-number",
                    viewData = PhoneNumberViewData(
                        countries = CountriesModel.create(
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