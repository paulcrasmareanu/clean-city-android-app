package com.upt.cleancity.model

import java.io.Serializable

data class LoginContract(
        var username: String = "",
        var password: String = ""
) : Serializable