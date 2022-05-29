package com.upt.cleancity.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.upt.cleancity.R
import com.upt.cleancity.model.User
import com.upt.cleancity.service.UserService
import com.upt.cleancity.service.factory.UserServiceFactory
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var userService: UserService

    companion object {
        const val TAG = "__RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userService = UserServiceFactory.makeService(this)
    }

    fun performButtonFunctionalities(view: View) {
        when (view.id) {
            R.id.registerSubmitButton -> createUser(registerEmail.editText?.text.toString(), registerFirstName.editText?.text.toString(), registerLastName.editText?.text.toString(),
                                                    registerPassword.editText?.text.toString(), registerPasswordConfirm.editText?.text.toString())
            R.id.loginRedirectButton -> finish()
        }
    }

    private fun createUser(email: String, firstName: String, lastName: String, password: String, passwordConfirm: String) {
        Log.d(TAG, "Register works!")
        if(!isValidForm(email, firstName, lastName, password, passwordConfirm)) {
            Log.w(TAG, "User registration not valid")
            Toast.makeText(this, "Fields must not be blank", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(
            "",
            email = email,
            firstName = firstName,
            lastName = lastName,
            password = password,
            confirmPassword = passwordConfirm,
            role = 1
        )

        createUser(user)
    }

    private fun isValidForm(email: String, firstName: String, lastName: String, password: String, passwordConfirm: String): Boolean {
        when {
            email.isBlank() -> this.registerEmail?.error = "Required"
            firstName.isBlank() -> this.registerFirstName?.error = "Required"
            lastName.isBlank() -> this.registerLastName?.error = "Required"
            password.isBlank() -> this.registerPassword?.error = "Required"
            password.length < 6 -> this.registerPassword?.error = "Password must be at least 6 characters long"
            passwordConfirm.isBlank() -> this.registerPasswordConfirm?.error = "Required"
            passwordConfirm.length < 6 -> this.registerPasswordConfirm?.error = "Password must be at least 6 characters long"
            password != passwordConfirm -> this.registerPassword?.error = "Passwords must match"
            else -> return true
        }
        return false
    }

    private fun createUser(user: User) {
        userService.createNewUser(user).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.w(TAG, "createNewUser: onFailed()", t)
                Toast.makeText(this@RegisterActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d(TAG, "createNewUser: onResponse()")

                if (response.body() == null) {
                    Log.d(TAG, "Successful code: " + response.code())
                    Toast.makeText(this@RegisterActivity, "Successfully registered", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.w(TAG, "Failed code: " + response.code())
                    Log.w(TAG, response.body().toString())
                    Toast.makeText(this@RegisterActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })
    }
}