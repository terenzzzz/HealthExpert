package com.example.healthExpert.utils

class CaloriesCalc {

//    Calories Burned = (Weight(kg) x Distance(km) x 0.621371) x MET Value
//    Bicycling:  Met = 7.5 (01015)
//    Running:   Met = 7.0 (12020)
//    Walking:    Met = 3.5 (17160)



    fun calories(weight:Float,distance:Float,type:String): Float {
        var calories = 0F
        when(type){
            "Walking" -> calories = weight.times(distance).times(0.621371F).times(3.5F)
            "Running" -> calories = weight.times(distance).times(0.621371F).times(7.0F)
            "Cycling" -> calories = weight.times(distance).times(0.621371F).times(7.5F)
        }
        return calories
    }

}