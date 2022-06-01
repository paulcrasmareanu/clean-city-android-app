package com.upt.cleancity.model

import java.io.Serializable

data class Issue (
    var id: String? = null,
    var title: String = "",
    var description: String = "",
    var status: Int = 1,
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var ownerId: String = "",
    var attachmentUrl: String? = ""
) : Serializable