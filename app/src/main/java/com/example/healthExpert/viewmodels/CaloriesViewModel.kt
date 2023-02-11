package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.User
import com.example.healthExpert.repository.CaloriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CaloriesViewModel(private val activity: AppCompatActivity) : ViewModel() {
    private val repository = CaloriesRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var calories = MutableLiveData<MutableList<Calories>?>()

    fun getCaloriesInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getCaloriesInfo(it) }

            // Refresh UI Update data
            calories.postValue(updatedData)
        }
    }

    fun addCalories(type:String,title:String,content:String,calories:Int,time:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.addCalories(token,type,title,content,calories,time)
            }

            // Refresh UI Update data
            getCaloriesInfo()
        }
    }
}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class CaloriesViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaloriesViewModel::class.java)) {
            return CaloriesViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}