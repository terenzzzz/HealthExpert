package com.example.healthExpert.model

import java.util.*

data class CaloriesOverall (
    var id: Int = -1,
    var idUser: Int = -1,
    var Intake: Int = 0,
    var Burn: Int = 0,
    var Date: Date = Date()
)


