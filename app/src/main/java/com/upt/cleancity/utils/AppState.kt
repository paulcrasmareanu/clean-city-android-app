package com.upt.cleancity.utils

import com.google.firebase.storage.FirebaseStorage
import com.upt.cleancity.model.Token
import com.upt.cleancity.model.User

object AppState {
    const val API_BASE_URL = "https://cleancityadministration20220602012919.azurewebsites.net/"
    var loggedInUser = User()
    var currentToken = Token()
    var storageReference = FirebaseStorage.getInstance().reference
}