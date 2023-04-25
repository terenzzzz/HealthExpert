package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.Goal
import com.example.healthExpert.model.News
import com.example.healthExpert.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeartRateViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    var requestStatus = MutableLiveData<Int>()

    var bpm = MutableLiveData<String>()

    fun setBpm(bpm:String){
        this.bpm.postValue(bpm)
    }





}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class HeartRateViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeartRateViewModel::class.java)) {
            return HeartRateViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}