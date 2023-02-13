package com.example.healthExpert.model

import java.util.*

data class Trainings(
    var id: Int = -1,
    var idUser: Int = -1,
    var Type: String = "",
    var Title: String = "",
    var Distance: Float = 0f,
    var Speed: Float = 0f,
    var CaloriesBurn: Int = 0,
    var StartTime: Date = Date(),
    var EndTime: Date = Date(),
)