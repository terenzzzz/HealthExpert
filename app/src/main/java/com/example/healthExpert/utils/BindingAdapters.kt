package com.example.healthExpert.utils

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*


object BindingAdapters {
    @BindingConversion
    @JvmStatic
    fun convertFloatToString(value: Float): String {
        return value.toString()
    }

    @BindingConversion
    @JvmStatic
    fun convertIntToString(value: Int): String {
        return value.toString()
    }

    @BindingConversion
    @JvmStatic
    fun convertLiveDataFloatToString(liveData: MutableLiveData<Float>): String {
        return liveData.value.toString()
    }


}