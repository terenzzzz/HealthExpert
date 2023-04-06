package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.News
import com.example.healthExpert.model.Sleep
import com.example.healthExpert.model.Trainings
import com.example.healthExpert.repository.NewsRepository
import com.example.healthExpert.repository.SleepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SleepViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    var requestStatus = MutableLiveData<Int>()

    private val repository = SleepRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var timer = MutableLiveData<String>()
    var sleep = MutableLiveData<Sleep?>()

    fun updateTimer(time:String){
        this.timer.value = time
    }

    fun getSleep(date: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val sleepParse = token?.let { repository.getSleep(it,date) }

            // Refresh UI Update data
            if (sleepParse != null) {
                if (sleepParse.status != 200){
                    requestStatus.postValue(sleepParse.status)
                }
                sleep.postValue(sleepParse.data)
            }
        }
    }

    fun addSleep(temperature:Float,pressure:Float,light:Float,
                 humidity:Float,startTime:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.addSleep(token,temperature,pressure,light,humidity,startTime)
            }
        }
    }

}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class SleepViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepViewModel::class.java)) {
            return SleepViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}