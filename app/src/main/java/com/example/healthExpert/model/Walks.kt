package com.example.healthExpert.model

import java.util.*

data class Walks (
    var id: Int = -1,
    var idUser: Int = -1,
    var TotalSteps: Int = 0,
    var Calories: Int = 0,
    var Distance: Int = 0,
    var Date: Date = Date()
)