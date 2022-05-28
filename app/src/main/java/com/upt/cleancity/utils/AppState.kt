package com.upt.cleancity.utils

import com.upt.cleancity.model.Token
import com.upt.cleancity.model.User

object AppState {
    const val API_BASE_URL = "https://10.0.2.2:7222/"
    var loggedInUser = User()
    var currentToken = Token()
}