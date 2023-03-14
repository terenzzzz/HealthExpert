package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.CaloriesOverall
import com.example.healthExpert.repository.CaloriesRepository
import com.example.healthExpert.repository.TrainingsRepository
import com.example.healthExpert.repository.WalkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CaloriesViewModel(private val activity: AppCompatActivity) : ViewModel() {
    private val repository = CaloriesRepository()
    private val walkRepository = WalkRepository()
    private val trainingsRepository = TrainingsRepository()

    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var calories = MutableLiveData<MutableList<Calories>?>()
    var caloriesInfo = MutableLiveData<Calories?>()
    var caloriesAll = MutableLiveData<CaloriesOverall?>()

    var stepsCalories = MutableLiveData(0)
    var trainingCalories = MutableLiveData(0)




    fun getCaloriesOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null){
                val updatedData = repository.getCaloriesOverall(token,date)
                val updateStepsCalories = walkRepository.getWalksOverall(token,date)?.Calories
                val updateTrainingCalories = trainingsRepository.getTrainingOverall(token,date)?.Calories?.toInt()


                if (updatedData!=null && updateStepsCalories!=null && updateTrainingCalories!=null){
                    updatedData.Burn = updatedData.Burn + updateStepsCalories + updateTrainingCalories
                }
                // Refresh UI Update data
                caloriesAll.postValue(updatedData)
                stepsCalories.postValue(updateStepsCalories)
                trainingCalories.postValue(updateTrainingCalories)
            }

        }
    }

    fun updateCaloriesOverall(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if(token != null){
                repository.updateCaloriesOverall(token)
            }
        }
    }



    fun getCalories(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getCalories(it) }

            // Refresh UI Update data
            calories.postValue(updatedData)
        }
    }

    fun getCaloriesInfo(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            Log.w("Calories", "Called: getCaloriesInfo")
            val updatedData = token?.let { repository.getCaloriesInfo(it,id) }

            // Refresh UI Update data
            caloriesInfo.postValue(updatedData)
        }
    }

    fun addCalories(type:String,title:String,content:String,calories:Int,time:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.addCalories(token,type,title,content,calories,time)
            }
        }
    }

    fun editCaloriesType(id: Int,type: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesType(token, id , type)
            }
        }
    }

    fun editCaloriesTitle(id: Int,title: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesTitle(token, id , title)
            }
        }
    }

    fun editCaloriesContent(id: Int,content: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesContent(token, id , content)
            }
        }
    }

    fun editCaloriesCalories(id: Int,calories: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesCalories(token, id , calories)
            }
        }
    }

    fun editCaloriesTime(id: Int,time: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesTime(token, id , time)
            }
        }
    }

    fun deleteCalories(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.deleteCalories(token, id)
                getCalories()
            }
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