package com.upt.cleancity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.upt.cleancity.R

class EditIssueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_issue)
    }

    fun performButtonFunctionalities(view: View) {
        when (view.id) {
            R.id.issueEditBackButton -> finish()
            R.id.issueEditSubmitButton -> updateIssue()
        }
    }

    private fun updateIssue() {

    }
}