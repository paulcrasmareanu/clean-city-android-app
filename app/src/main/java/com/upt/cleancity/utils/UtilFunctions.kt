package com.upt.cleancity.utils

import android.net.Uri
import android.text.Editable

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun convertStringToUri(string: String) = Uri.parse(string)

fun convertUriToString(uri: Uri) = uri.toString()