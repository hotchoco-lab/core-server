package org.hotchoco.core.register.route

import io.ktor.server.routing.*
import routeRegisterPhoneNumber

fun Route.routeRegister() {
    routeRegisterNew()
    routeRegisterTerms()
    routeRegisterPhoneNumber()
    routeRegisterPasscode()
}