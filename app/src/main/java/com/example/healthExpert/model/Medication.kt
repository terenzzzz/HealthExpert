package com.example.healthExpert.model

import java.util.Date

data class Medication (
    var id: Int,
    var idUser: Int,
    var Type: String,
    var Name: String,
    var Dose: Float,
    var Date: Date,
)