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
import com.upt.cleancity.utils.AppState
import kotlinx.android.synthetic.main.activity_create_issue.*
import kotlinx.android.synthetic.main.issue_create_edit_photo_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateIssueActivity : AppCompatActivity() {

    companion object {
        const val TAG = "__CreateIssueActivity"
        const val ADD_MARKER = 200
        const val IMAGE_PICK = 100
    }

    private lateinit var issueService: IssueService

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var userId = AppState.loggedInUser.id
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_issue)

        issueService = IssueServiceFactory.makeService(this)

        val intent = intent
        latitude = intent.getDoubleExtra("MARKER_LATITUDE", 0.0)
        longitude = intent.getDoubleExtra("MARKER_LONGITUDE", 0.0)

        removePictureButton.setOnClickListener {
            imageUri = null
            issueImage.setImageURI(null)
            issueCreateNoImageSelected.visibility = View.VISIBLE
            issuePictureLayout.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK && data != null) {
            imageUri = data.data
            issueImage.setImageURI(imageUri)
            issueCreateNoImageSelected.visibility = View.GONE
            issuePictureLayout.visibility = View.VISIBLE
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun performButtonFunctionalities(view: View) {
        when (view.id) {
            R.id.issueSubmitButton -> createIssue(issueCreateTitle.editText?.text.toString(), issueCreateDescription.editText?.text.toString())
            R.id.issueCreateBackButton -> finish()
            R.id.issueCreateAddPictureButton -> addImageToView()
        }
    }

    private fun addImageToView() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, IMAGE_PICK)
    }

    private fun createIssue(title: String, description: String) {
        if (!isValidForm()) {
            Log.w(TAG, "Invalid issue fields")
            Toast.makeText(this, "Fields must not be blank", Toast.LENGTH_SHORT).show()
            return
        }

        val issue = Issue(
            title = title,
            description = description,
            lat = latitude,
            long = longitude,
            ownerId = userId
        )

        issueService.saveIssue(issue).enqueue(object : Callback<Issue> {
            override fun onResponse(call: Call<Issue>, response: Response<Issue>) {
                Log.d(TAG, "saveIssue: onResponse()")
                if (response.code() == 200) {
                    val savedIssue = response.body()!!
                    val intent = Intent()
                    intent.putExtra("ISSUE_ID", savedIssue.id)
                    intent.putExtra("ISSUE_LATITUDE", savedIssue.lat)
                    intent.putExtra("ISSUE_LONGITUDE", savedIssue.long)
                    setResult(ADD_MARKER, intent)
                    finish()
                } else {
                    Log.w(TAG, "Error code: ${response.code()}")
                    Toast.makeText(this@CreateIssueActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Issue>, t: Throwable) {
                Log.w(TAG, "saveIssue: onFailure()", t)
                Toast.makeText(this@CreateIssueActivity, "Failed to save issue", Toast.LENGTH_SHORT).show()
            }

        })
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