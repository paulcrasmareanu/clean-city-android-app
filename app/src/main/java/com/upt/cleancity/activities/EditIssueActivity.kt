package com.upt.cleancity.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.upt.cleancity.R
import com.upt.cleancity.model.Issue
import com.upt.cleancity.service.IssueService
import com.upt.cleancity.service.factory.IssueServiceFactory
import com.upt.cleancity.utils.toEditable
import kotlinx.android.synthetic.main.activity_edit_issue.*
import kotlinx.android.synthetic.main.issue_create_edit_photo_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditIssueActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__EditIssueActivity"
        const val UPDATE_ISSUE = 202
        const val IMAGE_PICK = 100
    }

    private lateinit var issueService: IssueService
    private lateinit var issue: Issue

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_issue)

        issueService = IssueServiceFactory.makeService(this)

        val intent = intent
        issue = intent!!.getSerializableExtra("ISSUE") as Issue

        removePictureButton.setOnClickListener {
            imageUri = null
            issueImage.setImageURI(null)
            issueEditNoImageSelected.visibility = View.VISIBLE
            issueEditPictureLayout.visibility = View.GONE
        }

        loadIssueDetails()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK && data != null) {
            imageUri = data.data
            issueImage.setImageURI(imageUri)
            issueEditNoImageSelected.visibility = View.GONE
            issueEditPictureLayout.visibility = View.VISIBLE
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun performButtonFunctionalities(view: View) {
        when (view.id) {
            R.id.issueEditBackButton -> finish()
            R.id.issueEditSubmitButton -> updateIssue()
            R.id.issueEditAddPictureButton -> addImageToView()
        }
    }

    private fun updateIssue() {
        if (!isValidForm()) {
            Log.w(TAG, "Invalid issue edit fields")
            Toast.makeText(this, "Fields must not be blank", Toast.LENGTH_SHORT).show()
            return
        }

        issue.title = issueEditTitle.editText!!.text.toString()
        issue.description = issueEditDescription.editText!!.text.toString()

        issueService.saveIssue(issue).enqueue(object : Callback<Issue> {
            override fun onResponse(call: Call<Issue>, response: Response<Issue>) {
                Log.d(TAG, "updateIssue: onResponse()")
                if (response.code() == 200 || response.code() == 204) {
                    setResult(UPDATE_ISSUE)
                    finish()
                } else {
                    Log.w(CreateIssueActivity.TAG, "Error code: ${response.code()}")
                    Toast.makeText(this@EditIssueActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Issue>, t: Throwable) {
                Log.w(TAG, "updateIssue: onFailure()", t)
                Toast.makeText(this@EditIssueActivity, "Failed to update issue", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addImageToView() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, IMAGE_PICK)
    }

    private fun isValidForm(): Boolean {
        when {
            issueEditTitle.editText?.text.toString().isBlank() -> this.issueEditTitle?.error = "Required"
            issueEditDescription.editText?.text.toString().isBlank() -> this.issueEditDescription?.error = "Required"
            else -> return true
        }
        return false
    }

    private fun loadIssueDetails() {
        issueEditTitle.editText?.text = issue.title.toEditable()
        issueEditDescription.editText?.text = issue.description.toEditable()
    }
}