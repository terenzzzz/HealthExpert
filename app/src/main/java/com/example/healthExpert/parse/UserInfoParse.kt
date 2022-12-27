package com.example.healthExpert.parse

import com.google.gson.annotations.SerializedName

class UserInfoParse {
    var status: Int? = null
    var message: String? = null
    var data: DataDTO? = null

    class DataDTO {
        var idUser: Int? = null

        @SerializedName("Email")
        var email: String? = null

        @SerializedName("Password")
        var password: String? = null

        @SerializedName("Name")
        var name: String? = null

        @SerializedName("Age")
        var age: Int? = null

        @SerializedName("Height")
        var height: Int? = null

        @SerializedName("Weight")
        var weight: Int? = null
    }
}
