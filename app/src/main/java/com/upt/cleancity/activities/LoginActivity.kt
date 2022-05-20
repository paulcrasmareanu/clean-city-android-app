package com.upt.cleancity.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import com.upt.cleancity.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun performButtonFunctionalities(view: View) {
        when(view.id) {
            R.id.registerRedirectButton -> registerUser()
            R.id.loginSubmitButton -> loginUser(loginEmail.text.toString(), loginPassword.text.toString())
        }
        Log.d("LOGIN", "LOGIN WORKS")
    }

    private fun registerUser() {
        Log.d("LOGIN", "REDIRECTING TO REGISTER")
    }

    private fun loginUser(username: String, password: String) {
        Log.d("LOGIN", "$username $password")
    }
}