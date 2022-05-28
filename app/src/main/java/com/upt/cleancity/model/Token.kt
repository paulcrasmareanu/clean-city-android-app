package com.upt.cleancity.model

import java.io.Serializable

data class Token (
        var tokenType: String = "",
        var expiresIn: Int = 0,
        var extExpiresIn: Int = 0,
        var accessToken: String = "",
        var idToken: String = "",
        var role: Int = 0
) : Serializable