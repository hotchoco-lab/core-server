package org.hotchoco.core.register.util

import org.hotchoco.core.model.AlertDataModel
import org.hotchoco.core.model.ButtonModel
import org.hotchoco.core.register.response.AccountResponse

inline fun fallbackRegister() = AccountResponse<Unit>(
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