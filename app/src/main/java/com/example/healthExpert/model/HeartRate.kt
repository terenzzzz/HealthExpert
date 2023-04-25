package com.example.healthExpert.model

import java.util.*

data class HeartRate(
    var id: Int = -1,
    var idUser: Int = -1,
    var HeartRate: String = "",
    var Date: Date = Date()
)