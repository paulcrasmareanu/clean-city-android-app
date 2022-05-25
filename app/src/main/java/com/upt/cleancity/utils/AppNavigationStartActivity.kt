package com.upt.cleancity.utils

import android.content.Context
import android.content.Intent
import com.upt.cleancity.activities.MapsActivity
import com.upt.cleancity.activities.RegisterActivity

class AppNavigationStartActivity {
    companion object {
        fun transitionToRegister(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }

        fun transitionToMaps(context: Context) {
            context.startActivity(Intent(context, MapsActivity::class.java))
        }
    }
}