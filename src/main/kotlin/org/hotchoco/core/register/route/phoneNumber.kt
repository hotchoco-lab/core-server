import com.google.i18n.phonenumbers.PhoneNumberUtil
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.hotchoco.core.helper.CoreHelper
import org.hotchoco.core.register.model.PhoneNumberModel
import org.hotchoco.core.register.model.RegisterLevel
import org.hotchoco.core.register.model.RegisterSession
import org.hotchoco.core.register.request.PhoneNumberRequest
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.viewdata.PasscodeViewData
import org.hotchoco.core.register.util.fallbackRegister

internal fun Route.routeRegisterPhoneNumber() {
    val registerLevelStorage = CoreHelper.registerLevelStorageGetter!!.invoke()
    val phoneNumberStorage = CoreHelper.phoneNumberStorageGetter!!.invoke()
    
    route("/android/account2") {
        post<PhoneNumberRequest>("/phone-number") {
            val session = call.sessions.get<RegisterSession>()
                    ?: return@post call.respond(fallbackRegister())
            
            try {
                val currentLevel = registerLevelStorage.read(session.uuid)

                if (currentLevel != RegisterLevel.TERMS.toString()) return@post call.respond(fallbackRegister())
            } catch (e: Throwable) {
                return@post call.respond(fallbackRegister())
            }
            
            registerLevelStorage.write(session.uuid, RegisterLevel.PHONE_NUMBER.toString())
            
            val phoneUtil = PhoneNumberUtil.getInstance()
            
            val phoneNumber = phoneUtil.parse(it.phoneNumber, it.countryIso)

            val parsedPhoneNumber = PhoneNumberModel(
                countryIso = it.countryIso,
                countryCode = it.countryCode,
                pstnNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164).removePrefix("+"),
                beautifiedPstnNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL),
                nsnNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL).replace(" ", "").replace("-", ""),
                beautifiedNsnNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL).replace(" ", "-"),
            )

            phoneNumberStorage[session.uuid] = parsedPhoneNumber
            
            call.respond(
                AccountResponse(
                    status = 0,
                    view = "passcode",
                    viewData = PasscodeViewData(
                        methods = listOf("sms", "voice"),
                        phoneNumber = parsedPhoneNumber,
                        retryTime = 60,
                        enenableAccessibilityArs = false
                    )
                )
            )
        }
    }
}