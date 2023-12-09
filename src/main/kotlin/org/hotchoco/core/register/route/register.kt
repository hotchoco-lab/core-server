package org.hotchoco.core.register.route

import io.ktor.server.routing.*

fun Route.routeRegister() {
    routeRegisterNew()
    routeRegisterTerms()
}