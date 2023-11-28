package org.hotchoco.core.subdevice.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.hotchoco.core.subdevice.response.SubDeviceWhitelistResponse

fun Route.routeSubDeviceWhitelist() {
    route("/android/account") {
        get("/allowlist") {
            val modelName = call.parameters["model_name"]

            val alwaysAllow = application.environment.config.property("hotchoco.subdevice.alwaysAllow")
                .getString()
                .toBooleanStrict()

            val allowList = application.environment.config.property("hotchoco.subdevice.models")
                .getList()
                .map { it.toString() }

            val allowListed = alwaysAllow || allowList.contains(modelName)

            call.respond(
                SubDeviceWhitelistResponse(
                    allowListed = allowListed
                )
            )
        }
    }
}