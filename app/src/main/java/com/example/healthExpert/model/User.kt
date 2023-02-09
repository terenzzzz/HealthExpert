package com.example.healthExpert.model

data class User(var idUser: Int = -1,
                var Email: String = "",
                var Password: String = "",
                var Name: String = "",
                var Gender: String = "",
                var Age: Int = 0,
                var Height: Float = 0f,
                var Weight: Float = 0f,
                var Bmi: Float = 0f,
                var BodyFactRate: Float = 0f,
)