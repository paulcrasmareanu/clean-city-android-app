package com.upt.cleancity.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import com.upt.cleancity.R
import com.upt.cleancity.model.LoginContract
import com.upt.cleancity.model.Token
import com.upt.cleancity.model.User
import com.upt.cleancity.service.factory.UserServiceFactory
import com.upt.cleancity.utils.AppNavigationStartActivity
import com.upt.cleancity.utils.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        } else {
            val contract = LoginContract(email, password)
            UserServiceFactory.makeService(this@LoginActivity).loginUser(contract).enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Log.w(TAG, "loginUser: onFailed()", t)
                    Toast.makeText(this@LoginActivity, "Something went wrong during login", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    Log.d(TAG, "loginUser: onResponse()")

                    if (response.body() != null) {
                        if (response.code() == 200) {
                            val token = response.body()!!
                            AppState.currentToken = token
                            Toast.makeText(this@LoginActivity, "Success: now redirect", Toast.LENGTH_SHORT).show()
                            //todo get user and redirect to maps
                        } else {
                            Log.w(TAG, "Failed code: " + response.code())
                            Log.w(TAG, response.body().toString())
                            Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
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