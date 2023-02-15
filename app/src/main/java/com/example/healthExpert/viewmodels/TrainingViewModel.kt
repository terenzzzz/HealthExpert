package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Location
import com.example.healthExpert.model.Trainings
import com.example.healthExpert.model.Weather
import com.example.healthExpert.repository.TrainingsRepository
import com.example.healthExpert.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime

class TrainingViewModel(private val activity: AppCompatActivity) : ViewModel() {
    private val repository = TrainingsRepository()
    private val weatherRepository = WeatherRepository()

    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")

    var trainings = MutableLiveData<MutableList<Trainings>?>()
    var trainingInfo = MutableLiveData<MutableList<Trainings>?>()
    var trainingLocations = MutableLiveData<MutableList<Location>?>()
    var weather = MutableLiveData<Weather>()
    var totalDistance = MutableLiveData<Float>()
    var totalBurn = MutableLiveData<Int>()
    var totalTime = MutableLiveData<Int>()


    fun getTrainings(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getTrainings(it) }

            // Refresh UI Update data
            trainings.postValue(updatedData)
        }
    }

    fun addTraining(type:String,
                    title:String,
                    distance:String,
                    speed:String,
                    caloriesBurn:String,
                    startTime:String,
                    endTime:String,
                    locations: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                val insertId = repository.addTraining(token,type, title, distance,
                    speed, caloriesBurn, startTime, endTime)
                repository.addLocations(token, insertId, locations)
            }
        }
    }

    fun getTrainingInfo(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                val updateData = repository.getTrainingInfo(token,id)
                trainingInfo.postValue(updateData)
            }
        }
    }

    fun getTrainingLocations(idTraining:Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                val updateData =repository.getTrainingLocations(token,idTraining)
                trainingLocations.postValue(updateData)
            }
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

    fun calcDashboard(){
        var totalDistance = 0f
        var totalBurn = 0
        var totalTime = 0
        for (item in trainings.value!!){
            totalDistance += item.Distance
            totalBurn += item.CaloriesBurn
            var startHour = SimpleDateFormat("HH").format(item.StartTime)
            var startMinus = SimpleDateFormat("mm").format(item.StartTime)
            var endHour = SimpleDateFormat("HH").format(item.EndTime)
            var endMinus = SimpleDateFormat("mm").format(item.EndTime)
            val duration = Duration.between(LocalTime.of(startHour.toInt(), startMinus.toInt()),
                LocalTime.of(endHour.toInt(), endMinus.toInt())).toMinutes()
            totalTime += duration.toInt()
        }
        this.totalTime.value = totalTime
        this.totalDistance.value = totalDistance
        this.totalBurn.value = totalBurn
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