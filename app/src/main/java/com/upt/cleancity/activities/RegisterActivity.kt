package com.upt.cleancity.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.upt.cleancity.R
import com.upt.cleancity.model.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
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
            0,
            email, firstName, lastName
        )

        //todo persist user to backend
    }

    private fun isValidForm(email: String, firstName: String, lastName: String, password: String, passwordConfirm: String): Boolean {
        when {
            email.isBlank() -> this.registerEmail?.error = "Required"
            firstName.isBlank() -> this.registerFirstName?.error = "Required"
            lastName.isBlank() -> this.registerLastName?.error = "Required"
            password.isBlank() -> this.registerPassword?.error = "Required"
            passwordConfirm.isBlank() -> this.registerPasswordConfirm?.error = "Required"
            else -> return true
        }
        return false
    }
}