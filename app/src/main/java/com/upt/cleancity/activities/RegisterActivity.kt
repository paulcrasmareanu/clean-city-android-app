package com.upt.cleancity.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.upt.cleancity.R

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
            R.id.registerSubmitButton -> createUser()
            R.id.loginRedirectButton -> finish()
        }
    }

    private fun createUser() {
        Log.d(TAG, "Register works!")
    }
}