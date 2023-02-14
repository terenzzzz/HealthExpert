package com.example.healthExpert.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Location (
    var latitude: Double,
    var longitude: Double
)

