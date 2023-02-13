package com.example.healthExpert.parse

import com.example.healthExpert.model.Trainings
import com.google.gson.annotations.SerializedName


data class TrainingsParse (
    var status: Int? = null,
    var message: String? = null,
    var data: List<Trainings>? = null
)