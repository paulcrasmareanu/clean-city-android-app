package com.upt.cleancity.model

import java.io.Serializable

data class Issue (
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var status: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var ownerId: Long = 0
) : Serializable