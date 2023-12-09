import com.google.i18n.phonenumbers.PhoneNumberUtil
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.hotchoco.core.helper.CoreHelper
import org.hotchoco.core.model.AlertDataModel
import org.hotchoco.core.model.ButtonModel
import org.hotchoco.core.register.model.PhoneNumberModel
import org.hotchoco.core.register.model.RegisterLevel
import org.hotchoco.core.register.model.RegisterSession
import org.hotchoco.core.register.request.PhoneNumberRequest
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.response.PhoneNumberResponse

internal fun Route.routeRegisterPhoneNumber() {
    val registerLevelStorage = CoreHelper.registerLevelStorageGetter!!.invoke()
    
    route("/android/account2") {
        post<PhoneNumberRequest>("/phone-number") {
            val session = call.sessions.get<RegisterSession>()
                    ?: return@post call.respond(
                        AccountResponse<Unit>(
                            status = 0,
                            message = "일시적인 오류로 처음으로 돌아갑니다. 다시 시도해 주세요.",
                            alertData = AlertDataModel(
                                title = null,
                                message = "일시적인 오류로 처음으로 돌아갑니다. 다시 시도해 주세요.",
                                buttons = listOf(
                                    ButtonModel(
                                        name = "확인",
                                        view = "login"
                                    )
                                )
                            )
                        )
                    )
            
            try {
                val currentLevel = registerLevelStorage.read(session.uuid)

                if (currentLevel != RegisterLevel.TERMS.toString()) return@post call.respond(
                    AccountResponse<Unit>(
                        status = 0,
                        message = "일시적인 오류로 처음으로 돌아갑니다. 다시 시도해 주세요.",
                        alertData = AlertDataModel(
                            title = null,
                            message = "일시적인 오류로 처음으로 돌아갑니다. 다시 시도해 주세요.",
                            buttons = listOf(
                                ButtonModel(
                                    name = "확인",
                                    view = "login"
                                )
                            )
                        )
                    )
                )
            } catch (e: Throwable) {
                return@post call.respond(
                    AccountResponse<Unit>(
                        status = 0,
                        message = "일시적인 오류로 처음으로 돌아갑니다. 다시 시도해 주세요.",
                        alertData = AlertDataModel(
                            title = null,
                            message = "일시적인 오류로 처음으로 돌아갑니다. 다시 시도해 주세요.",
                            buttons = listOf(
                                ButtonModel(
                                    name = "확인",
                                    view = "login"
                                )
                            )
                        )
                    )
                )
            }
            
            registerLevelStorage.write(session.uuid, RegisterLevel.PHONE_NUMBER.toString())
            
            val phoneUtil = PhoneNumberUtil.getInstance()
            
            val phoneNumber = phoneUtil.parse(it.phoneNumber, it.countryIso)
            
            call.respond(
                AccountResponse(
                    status = 0,
                    view = "passcode",
                    viewData = PhoneNumberResponse(
                        methods = listOf("sms", "voice"),
                        phoneNumber = PhoneNumberModel(
                            countryIso = it.countryIso,
                            countryCode = it.countryCode,
                            pstnNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164).removePrefix("+"),
                            beautifiedPstnNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL),
                            nsnNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL).replace(" ", "").replace("-", ""),
                            beautifiedNsnNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL).replace(" ", "-"),
                            ),
                        retryTime = 60,
                        enenableAccessibilityArs = false
                    )
                )
            )
        }
    }
}