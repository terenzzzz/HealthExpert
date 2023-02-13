package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Trainings
import com.example.healthExpert.model.Weather
import com.example.healthExpert.repository.TrainingsRepository
import com.example.healthExpert.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrainingViewModel(private val activity: AppCompatActivity) : ViewModel() {
    private val repository = TrainingsRepository()
    private val weatherRepository = WeatherRepository()

    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var trainings = MutableLiveData<MutableList<Trainings>?>()
    var weather = MutableLiveData<Weather>()


    fun getTrainings(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getTrainings(it) }

            // Refresh UI Update data
            trainings.postValue(updatedData)
        }
    }

    fun getWeather(latitude:Double,longitude:Double){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = weatherRepository.getWeather(latitude,longitude)

            // Refresh UI Update data
            weather.postValue(updatedData)
        }
    }

}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class TrainingViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingViewModel::class.java)) {
            return TrainingViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}