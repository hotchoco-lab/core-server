package org.hotchoco.core

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.hotchoco.core.plugins.*
import org.hotchoco.core.register.route.routeRegister
import org.hotchoco.core.subdevice.route.routeSubDeviceWhitelist

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureTemplating()
    configureDatabases()
    configureRouting()

    routing {
        routeRegister()
        routeSubDeviceWhitelist()
    }
}
