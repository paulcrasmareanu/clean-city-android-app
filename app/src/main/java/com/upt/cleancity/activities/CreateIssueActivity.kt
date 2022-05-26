package com.upt.cleancity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.upt.cleancity.R
import com.upt.cleancity.model.Issue
import kotlinx.android.synthetic.main.activity_create_issue.*

class CreateIssueActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__CreateIssueActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_issue)
    }

    fun performButtonFunctionalities(view: View) {
        when (view.id) {
            R.id.issueSubmitButton -> createIssue(issueCreateTitle.editText?.text.toString(), issueCreateDescription.editText?.text.toString())
            R.id.issueCreateBackButton -> finish()
        }
    }

    private fun createIssue(title: String, description: String) {
        if (!isValidForm()) {
            Log.w(TAG, "Invalid issue fields")
            Toast.makeText(this, "Fields must not be blank", Toast.LENGTH_SHORT).show()
            return
        }

        //todo pass lat and long to activity when creating issue
        val issue = Issue(
            0,
            title, description
        )

        //todo persist issue to backend
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