package com.example.healthExpert.model

import java.util.Date

data class News (
    var id: Int,
    var Title: String,
    var Content: String,
    var Date: Date,
    var Author: String,
    var Image: String
)