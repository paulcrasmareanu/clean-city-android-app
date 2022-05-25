package com.upt.cleancity.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import com.upt.cleancity.R
import com.upt.cleancity.utils.AppNavigationStartActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun performButtonFunctionalities(view: View) {
        Log.d(TAG, "LOGIN WORKS")
        when(view.id) {
            R.id.registerRedirectButton -> registerUser()
            R.id.loginSubmitButton -> loginUser(loginEmail.text.toString(), loginPassword.text.toString())
        }
    }

    private fun registerUser() {
        Log.d(TAG, "REDIRECTING TO REGISTER")
        AppNavigationStartActivity.transitionToRegister(this@LoginActivity)
    }

    private fun loginUser(email: String, password: String) {
        if (!isValidForm(email, password)) {
            Log.w(TAG, "User login invalid")
            Toast.makeText(this, "Fields must not be blank", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d(TAG, "$email $password")
    }

    private fun isValidForm(email: String, password: String): Boolean {
        when {
            email.isBlank() -> this.loginEmail?.error = "Required"
            password.isBlank() -> this.loginPassword?.error = "Required"
            else -> return true
        }
        return false
    }
}