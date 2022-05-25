package com.upt.cleancity.model

import java.io.Serializable

data class User (
    var id: Long = 0,
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var isActive: Boolean = true
) : Serializable