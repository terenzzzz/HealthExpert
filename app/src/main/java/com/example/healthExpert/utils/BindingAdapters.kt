package com.example.healthExpert.utils

import androidx.databinding.BindingConversion

object BindingAdapters {
    @BindingConversion
    @JvmStatic
    fun convertFloatToString(value: Float): String {
        return value.toString()
    }
}