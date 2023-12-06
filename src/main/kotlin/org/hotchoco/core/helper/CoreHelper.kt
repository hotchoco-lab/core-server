package org.hotchoco.core.helper

import io.ktor.server.sessions.*
import java.io.File

object CoreHelper {

    val registerSessionStorage = directorySessionStorage(File(".sessions"), cached = true)

}