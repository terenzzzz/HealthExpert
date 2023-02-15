package com.example.healthExpert.parse

import com.google.gson.annotations.SerializedName

class LocationParse {
    var status: Int? = null
    var message: String? = null
    var data: List<DataDTO>? = null

    class DataDTO {
        var id: Int? = null
        var idTraining: Int? = null

        @SerializedName("Latitude")
        var latitude: Double? = null

        @SerializedName("Longitude")
        var longitude: Double? = null
    }
}