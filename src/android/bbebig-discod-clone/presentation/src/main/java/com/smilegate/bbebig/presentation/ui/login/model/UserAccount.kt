package com.smilegate.bbebig.presentation.ui.login.model

import com.smilegate.bbebig.domain.model.LoginInfoDomainModel
import com.smilegate.bbebig.domain.model.param.LoginParam

data class UserAccount(
    val email: String,
    val password: String,
)

fun UserAccount.toParam(): LoginParam {
    return LoginParam(
        email = email,
        password = password,
    )
}

fun UserAccount.toDomainModel(): LoginInfoDomainModel {
    return LoginInfoDomainModel(
        email = email,
        password = password,
    )
}
