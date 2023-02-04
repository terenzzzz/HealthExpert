package com.example.healthExpert.utils

import androidx.databinding.BindingConversion
import androidx.lifecycle.MutableLiveData

object BindingAdapters {
    @BindingConversion
    @JvmStatic
    fun convertFloatToString(value: Float): String {
        return value.toString()
    }

    @BindingConversion
    @JvmStatic
    fun convertLiveDataFloatToString(liveData: MutableLiveData<Float>): String {
        return liveData.value.toString()
    }
}