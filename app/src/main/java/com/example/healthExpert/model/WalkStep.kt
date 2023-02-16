package com.example.healthExpert.model

import java.util.Date

data class WalkStep (
    var id: Int = -1,
    var idUser: Int = -1,
    var Steps: Int = 0,
    var Time: Date? = null
)