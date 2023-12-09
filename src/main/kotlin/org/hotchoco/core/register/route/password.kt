package org.hotchoco.core.register.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.hotchoco.core.helper.CoreHelper
import org.hotchoco.core.register.viewdata.ProfileViewData
import org.hotchoco.core.register.model.RegisterLevel
import org.hotchoco.core.register.model.RegisterSession
import org.hotchoco.core.register.request.PasswordRequest
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.util.fallbackRegister

internal fun Route.routeRegisterPassword() {
    val registerLevelStorage = CoreHelper.registerLevelStorageGetter!!.invoke()

    route("/android/account2") {
        post<PasswordRequest>("/password") {
            val session = call.sessions.get<RegisterSession>()
                ?: return@post call.respond(fallbackRegister())

            try {
                val currentLevel = registerLevelStorage.read(session.uuid)

                if (currentLevel != RegisterLevel.PASSCODE.toString()) return@post call.respond(fallbackRegister())
            } catch (e: Throwable) {
                return@post call.respond(fallbackRegister())
            }

            registerLevelStorage.write(session.uuid, RegisterLevel.PROFILE.toString())

            call.respond(
                AccountResponse(
                    status = 0,
                    view = "profile",
                    viewData = ProfileViewData()
                )
            )
        }
    }
}