package org.hotchoco.core.register.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.hotchoco.core.helper.CoreHelper
import org.hotchoco.core.model.AlertDataModel
import org.hotchoco.core.model.ButtonModel
import org.hotchoco.core.register.model.RegisterLevel
import org.hotchoco.core.register.model.RegisterSession
import org.hotchoco.core.register.request.PasscodeRequest
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.response.PasscodeResponse
import org.hotchoco.core.register.response.PhoneNumberResponse

internal fun Route.routeRegisterPasscode() {
    val registerLevelStorage = CoreHelper.registerLevelStorageGetter!!.invoke()
    val phoneNumberStorage = CoreHelper.phoneNumberStorageGetter!!.invoke()

    route("/android/account2") {
        post<PasscodeRequest>("/passcode") {
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

            try {
                val currentLevel = registerLevelStorage.read(session.uuid)

                if (currentLevel != RegisterLevel.PHONE_NUMBER.toString()) return@post call.respond(
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
            } catch (e: Throwable) {
                return@post call.respond(
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
            }

            registerLevelStorage.write(session.uuid, RegisterLevel.PASSCODE.toString())

            call.respond(
                AccountResponse(
                    status = 0,
                    view = "password",
                    viewData = PasscodeResponse(
                        phoneNumber = phoneNumberStorage[session.uuid] ?: return@post call.respond(
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
                    )
                )
            )
        }
    }
}