package com.example.healthExpert.model

import java.util.*

data class Calories(
    var id: Int = -1,
    var idUser: Int = -1,
    var Type: String = "",
    var Title: String = "",
    var Content: String = "",
    var Calories: Int = 0,
    var Time: Date = Date()
)