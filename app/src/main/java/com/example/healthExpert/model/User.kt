package com.example.healthExpert.model

data class User(var idUser: Int = -1,
                var Email: String = "",
                var Password: String = "",
                var Name: String = "",
                var Age: Int = 0,
                var Height: Float = 0f,
                var Weight: Float = 0f,
)