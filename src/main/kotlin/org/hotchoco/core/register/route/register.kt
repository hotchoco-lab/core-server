package org.hotchoco.core.register.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.hotchoco.core.register.model.TermModel
import org.hotchoco.core.register.response.AccountResponse
import org.hotchoco.core.register.response.NewResponse
import java.util.UUID

fun Route.routeRegister() {
    route("/android/account2") {
        get("/new") {
            call.response.apply {
                header(
                    "Set-SS",
                    "27d4e3b74b0d4a3a9146090f8088cbed52d5f00a80d846bdb39a0793a7f27c6c"
                )

                header(
                    "C",
                    UUID.randomUUID().toString()
                )

                header("Kakao", "Talk")
            }

            call.respond(
                AccountResponse(
                    status = 0,
                    view = "terms",
                    viewData = NewResponse(
                        terms = listOf(
                            TermModel(
                                title = "테스트 옵션입니다.",
                                description = "",
                                code = "TEST",
                                essential = true
                            )
                        )
                    )
                )
            )
        }
    }
}