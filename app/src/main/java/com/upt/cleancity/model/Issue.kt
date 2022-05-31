package com.upt.cleancity.model

import java.io.Serializable

data class Issue (
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var status: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var ownerId: String = ""
) : Serializable