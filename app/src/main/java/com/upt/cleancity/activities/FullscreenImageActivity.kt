package com.upt.cleancity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.upt.cleancity.R
import com.upt.cleancity.utils.convertStringToUri
import kotlinx.android.synthetic.main.activity_fullscreen_image.*

class FullscreenImageActivity : AppCompatActivity() {

    private lateinit var imageUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)

        imageUri = intent.getStringExtra("IMAGE_URI").toString()

        val requestOptions = RequestOptions()
            .transforms(CenterInside())
        Glide.with(this)
            .load(convertStringToUri(imageUri))
            .apply(requestOptions)
            .into(issueFullScreenPicture)
    }
}