package org.hotchoco.core.helper

import io.ktor.server.sessions.*
import org.hotchoco.core.register.model.PhoneNumberModel

object CoreHelper {
    var registerLevelStorageGetter: (() -> SessionStorage)? = null
    var phoneNumberStorageGetter: (() -> MutableMap<String, PhoneNumberModel>)? = null
    
    init {
        val registerLevelStorage = SessionStorageMemory()
        val phoneNumberStorage = mutableMapOf<String, PhoneNumberModel>()

        registerLevelStorageGetter = {
            registerLevelStorage
        }

        phoneNumberStorageGetter = {
            phoneNumberStorage
        }
    }
}