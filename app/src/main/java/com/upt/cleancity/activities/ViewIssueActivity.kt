package com.upt.cleancity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.upt.cleancity.R
import com.upt.cleancity.model.Issue
import com.upt.cleancity.service.IssueService
import com.upt.cleancity.service.factory.IssueServiceFactory
import com.upt.cleancity.utils.AppNavigationStartActivity
import com.upt.cleancity.utils.AppState
import kotlinx.android.synthetic.main.activity_view_issue.*

class ViewIssueActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__ViewIssueActivity"
    }

    private lateinit var selectedIssue: Issue
    private lateinit var issueService: IssueService

    private var userId = AppState.loggedInUser.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_issue)

        issueService = IssueServiceFactory.makeService(this)

        //todo add button visibility clauses e.g. user owns issue or not
    }

    fun performButtonFunctionalities(view: View) {
        when(view.id) {
            R.id.issueViewBackButton -> finish()
            R.id.issueViewEditButton -> goToEditActivity(selectedIssue)
            R.id.issueViewDeleteButton -> deleteIssue()
        }
    }

    private fun goToEditActivity(issue: Issue) {
        AppNavigationStartActivity.transitionToEditIssue(this, issue)
    }

    private fun deleteIssue() {
        //todo add service method to delete issue
        finish()
    }
}