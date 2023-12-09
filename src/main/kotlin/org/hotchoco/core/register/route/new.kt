package org.hotchoco.core.register.route

import com.benasher44.uuid.uuid4
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.hotchoco.core.helper.CoreHelper
import org.hotchoco.core.register.model.RegisterLevel
import org.hotchoco.core.register.model.RegisterSession
import org.hotchoco.core.register.model.TermsListModel
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.util.listTerms

internal fun Route.routeRegisterNew() {
    val registerLevelStorage = CoreHelper.registerLevelStorageGetter!!.invoke()
    
    route("/android/account2") {
        get("/new") {
            val sessionKey = uuid4().toString()
            
            call.sessions.set(
                RegisterSession(
                    uuid = sessionKey
                )
            )
            
            registerLevelStorage.write(sessionKey, RegisterLevel.NEW.toString())

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
    }
}