package com.upt.cleancity.model

import java.io.Serializable

data class User (
    var id: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var role: Int = 0,
    var isActive: Boolean = true
) : Serializable