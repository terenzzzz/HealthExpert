package com.example.healthExpert.model

import java.util.*

data class TrainingOverall (
    var id: Int = -1,
    var idUser: Int = -1,
    var Distance: Float = 0f,
    var Speed: Float = 0f,
    var Calories: Float = 0f,
    var Duration: Int = 0,
    var Date: Date = Date()
)


