package com.example.healthExpert.parse

import com.example.healthExpert.model.Trainings
import com.google.gson.annotations.SerializedName


class TrainingsParse {
    var status: Int = -1
    var message: String? = null
    var data: List<Trainings>? = null
}