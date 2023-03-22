package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.*
import com.example.healthExpert.repository.TrainingsRepository
import com.example.healthExpert.repository.WeatherRepository
import com.example.healthExpert.utils.CaloriesCalc
import com.example.healthExpert.utils.DateTimeConvert
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
    private val weight = sharedPreferences.getFloat("weight",0F)

    var trainings = MutableLiveData<MutableList<Trainings>?>()
    var trainingInfo = MutableLiveData<MutableList<Trainings>?>()
    var trainingLocations = MutableLiveData<MutableList<Location>?>()
    var weather = MutableLiveData<Weather>()
    var trainingAll = MutableLiveData<TrainingOverall?>()

    // Training Record data
    var totalDistance = MutableLiveData(0F)
    var currentSpeed = MutableLiveData(0F)
    var totalCalories = MutableLiveData(0F)
    var timer = MutableLiveData("00:00:00")
    var speedList :MutableList<Float> = mutableListOf(0f)


    fun updateDistance(newDistance:Float){
        // Sum the distance (Unit KM)
        totalDistance.postValue(newDistance.div(1000).plus(totalDistance.value!!))
    }

    fun updateSpeed(newDistance:Float,lastTimer:String,timer: String){
        val lastTime = DateTimeConvert().HHmmsstoSeconds(lastTimer)
        val time = DateTimeConvert().HHmmsstoSeconds(timer)
        val timeDiff = time - lastTime
        val speed = newDistance.div(timeDiff).times(3.6F)  // km/h
        currentSpeed.postValue(speed)
        speedList.add(speed)
    }

    fun updateCalories(newDistance:Float,type: String){
        if(weight != null) {
            val calories = CaloriesCalc().calories(weight,newDistance.div(1000),type)
            totalCalories.postValue(totalCalories.value?.plus(calories) ?: 0F)
        }
    }

    fun updateTimer(time:String){
        timer.value = time
    }


    fun getTrainingOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getTrainingOverall(it,date) }

            // Refresh UI Update data
            trainingAll.postValue(updatedData)
        }
    }

    fun updateTrainingOverall(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if(token != null){
                repository.updateWaterOverall(token)
            }
        }
    }


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
                    startTime:String,
                    endTime:String,
                    locations: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                val distance = String.format("%.2f",this@TrainingViewModel.totalDistance.value)

                var speed = String.format("%.2f",this@TrainingViewModel.speedList.average())

                val calories = String.format("%.2f",this@TrainingViewModel.totalCalories.value)

                val insertId = repository.addTraining(token,type, title, distance,
                    speed, calories,
                    startTime, endTime, locations)
//                repository.addLocations(token, insertId, locations)
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
//
    fun deleteTraining(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                val resStatus = repository.deleteTraining(token,id)
                if (resStatus == 200){
                    repository.deleteTrainingLocation(token,id)
                }
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