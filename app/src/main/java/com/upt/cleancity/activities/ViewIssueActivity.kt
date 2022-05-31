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
import com.upt.cleancity.utils.AppNavigationStartActivity
import com.upt.cleancity.utils.AppState
import kotlinx.android.synthetic.main.activity_view_issue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewIssueActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__ViewIssueActivity"
    }

    private lateinit var selectedIssue: Issue
    private lateinit var issueService: IssueService
    private lateinit var issueId: String

    private var userId = AppState.loggedInUser.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_issue)

        issueService = IssueServiceFactory.makeService(this)
        val intent = intent
        issueId = intent.getStringExtra("ISSUE_ID").toString()

        issueService.getIssue(issueId).enqueue(object : Callback<Issue> {
            override fun onResponse(call: Call<Issue>, response: Response<Issue>) {
                Log.d(TAG, "getIssue: onResponse()")
                if (response.code() == 200) {
                    selectedIssue = response.body()!!
                    loadIssueDetails(selectedIssue)
                } else {
                    Log.w(TAG, "Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Issue>, t: Throwable) {
                Log.w(TAG, "getIssue: onFailure()", t)
                Toast.makeText(this@ViewIssueActivity, "Failed to retrieve issue", Toast.LENGTH_SHORT).show()
            }

        })
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

    private fun loadIssueDetails(issue: Issue) {
        issueViewTitle.text = issue.title
        issueViewDescription.text = issue.description
        if (issue.id == userId) {
            issueViewEditButton.visibility = View.VISIBLE
            issueViewDeleteButton.visibility = View.VISIBLE
        } else {
            issueViewEditButton.visibility = View.INVISIBLE
            issueViewDeleteButton.visibility = View.INVISIBLE
        }
    }
}