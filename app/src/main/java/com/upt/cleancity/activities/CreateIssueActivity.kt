package com.upt.cleancity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.upt.cleancity.R
import com.upt.cleancity.model.Issue
import com.upt.cleancity.service.IssueService
import com.upt.cleancity.service.factory.IssueServiceFactory
import kotlinx.android.synthetic.main.activity_create_issue.*

class CreateIssueActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__CreateIssueActivity"
    }

    private lateinit var issueService: IssueService

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_issue)

        issueService = IssueServiceFactory.makeService(this)

        val intent = intent
        latitude = intent.getDoubleExtra("MARKER_LATITUDE", 0.0)
        longitude = intent.getDoubleExtra("MARKER_LONGITUDE", 0.0)
    }

    fun performButtonFunctionalities(view: View) {
        when (view.id) {
            R.id.issueSubmitButton -> createIssue(issueCreateTitle.editText?.text.toString(), issueCreateDescription.editText?.text.toString())
            R.id.issueCreateBackButton -> finish() //todo remove marker in this case
        }
    }

    private fun createIssue(title: String, description: String) {
        if (!isValidForm()) {
            Log.w(TAG, "Invalid issue fields")
            Toast.makeText(this, "Fields must not be blank", Toast.LENGTH_SHORT).show()
            return
        }

        val issue = Issue(
            0,
            title, description,
            latitude = latitude,
            longitude = longitude
        )

        //todo persist issue to backend

        finish()
    }

    private fun isValidForm(): Boolean {
        when {
            issueCreateTitle.editText?.text.toString().isBlank() -> this.issueCreateTitle?.error = "Required"
            issueCreateDescription.editText?.text.toString().isBlank() -> this.issueCreateDescription?.error = "Required"
            else -> return true
        }
        return false
    }
}