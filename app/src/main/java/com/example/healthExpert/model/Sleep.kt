package com.example.healthExpert.model

import java.util.*

data class Sleep (
    var id: Int = -1,
    var idUser: Int = -1,
    var Temperature: Float = 0f,
    var Pressure: Float = 0f,
    var Light: Float = 0f,
    var Humidity: Float = 0f,
    var StartTime: Date = Date(),
    var EndTime: Date = Date()
)