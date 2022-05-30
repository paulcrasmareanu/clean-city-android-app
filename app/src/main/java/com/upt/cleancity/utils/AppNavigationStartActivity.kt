package com.upt.cleancity.utils

import android.content.Context
import android.content.Intent
import com.upt.cleancity.activities.*
import com.upt.cleancity.model.Issue

class AppNavigationStartActivity {
    companion object {
        fun transitionToRegister(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }

        fun transitionToMaps(context: Context) {
            context.startActivity(Intent(context, MapsActivity::class.java))
        }

        fun transitionToCreateIssue(context: Context, lat: Double, long: Double) {
            val intent = Intent(context, CreateIssueActivity::class.java)
            intent.putExtra("MARKER_LATITUDE", lat)
            intent.putExtra("MARKER_LONGITUDE", long)
            context.startActivity(intent)
        }

        fun transitionToViewIssue(context: Context) {
            context.startActivity(Intent(context, ViewIssueActivity::class.java))
        }

        fun transitionToEditIssue(context: Context, issue: Issue) {
            val intent = Intent(context, EditIssueActivity::class.java)
            intent.putExtra("ISSUE", issue)
            context.startActivity(intent)
        }
    }
}