package com.upt.cleancity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.upt.cleancity.R
import com.upt.cleancity.model.Issue
import com.upt.cleancity.service.IssueService
import com.upt.cleancity.service.factory.IssueServiceFactory
import com.upt.cleancity.utils.AppState
import com.upt.cleancity.utils.convertStringToUri
import kotlinx.android.synthetic.main.activity_view_issue.*
import kotlinx.android.synthetic.main.issue_photo_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewIssueActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__ViewIssueActivity"
        const val DELETE_MARKER = 201
        const val UPDATE_ISSUE = 202
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

        getIssueFromDatabase()
    }

    override fun onResume() {
        super.onResume()

        getIssueFromDatabase()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == UPDATE_ISSUE) {
            getIssueFromDatabase()
//            recreate()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun performButtonFunctionalities(view: View) {
        when(view.id) {
            R.id.issueViewBackButton -> finish()
            R.id.issueViewEditButton -> goToEditActivity(selectedIssue)
            R.id.issueViewDeleteButton -> deleteIssue()
        }
    }

    private fun getIssueFromDatabase() {
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

    private fun goToEditActivity(issue: Issue) {
        val intent = Intent(this, EditIssueActivity::class.java)
        intent.putExtra("ISSUE", issue)
        startActivityForResult(intent, UPDATE_ISSUE)
    }

    private fun deleteIssue() {
        issueService.deleteIssue(issueId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d(TAG, "deleteIssue: onResponse()")

                if (response.code() == 200 || response.code() == 204) {
                    val storageReference = AppState.storageReference.child("issues")
                        .child(issueId)
                    storageReference.delete().addOnSuccessListener {
                        Log.d(EditIssueActivity.TAG, "Successfully deleted issue image")
                    }.addOnFailureListener {
                        Log.w(EditIssueActivity.TAG, "Something went wrong during picture deletion", it)
                    }

                    val intent = Intent()
                    intent.putExtra("ISSUE_ID", issueId)
                    setResult(DELETE_MARKER, intent)
                    finish()
                } else {
                    Log.w(TAG, "Error code: ${response.code()}")
                    Toast.makeText(this@ViewIssueActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.w(TAG, "deleteIssue: onFailure()", t)
                Toast.makeText(this@ViewIssueActivity, "Failed to delete issue", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun loadIssueDetails(issue: Issue) {
        issueViewTitle.text = issue.title
        issueViewDescription.text = issue.description

        issue.attachmentUrl?.let {
            if (it.isNotBlank()) {
                issueViewPicLayout.visibility = View.VISIBLE

                Glide.with(this)
                    .load(convertStringToUri(issue.attachmentUrl!!))
                    .into(issueViewImage)

                issueViewImage.setOnClickListener {
                    val fullScreenIntent = Intent(this, FullscreenImageActivity::class.java)
                    fullScreenIntent.putExtra("IMAGE_URI", issue.attachmentUrl)
                    startActivity(fullScreenIntent)
                }
            }
        }

        if (issue.ownerId == userId) {
            issueViewEditButton.visibility = View.VISIBLE
            issueViewDeleteButton.visibility = View.VISIBLE
        } else {
            issueViewEditButton.visibility = View.GONE
            issueViewDeleteButton.visibility = View.GONE
        }
    }
}