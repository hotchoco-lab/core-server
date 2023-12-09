package org.hotchoco.core.helper

import io.ktor.server.sessions.*

object CoreHelper {
    var registerLevelStorageGetter: (() -> SessionStorage)? = null
    
    init {
        val registerLevelStorage = SessionStorageMemory()
        
        registerLevelStorageGetter = {
            registerLevelStorage
        }
    }
}