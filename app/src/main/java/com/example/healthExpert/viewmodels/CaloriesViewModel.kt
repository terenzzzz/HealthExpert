package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

class CaloriesViewModel(private val activity: AppCompatActivity) : ViewModel() {
    var requestStatus = MutableLiveData<Int>()

    private val repository = CaloriesRepository()
    private val walkRepository = WalkRepository()
    private val trainingsRepository = TrainingsRepository()

    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var calories = MutableLiveData<MutableList<Calories>?>()
    var caloriesInfo = MutableLiveData<Calories?>()
    var caloriesAll = MutableLiveData<CaloriesOverall?>()

    // Activity data
    var activityCalories = MutableLiveData(0) // without cycling


    fun getCaloriesOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null){
                // 获取卡路里汇总数据
                val caloriesOverallParse = repository.getCaloriesOverall(token,date)
                // 获取步数卡路里数据
                val walksOverallParse = walkRepository.getWalksOverall(token,date)
                // 获取训练数据
                val trainingsParse = trainingsRepository.getTrainings(token,date)

                if (caloriesOverallParse.status!=200 || walksOverallParse.status!=200 || trainingsParse.status!=200){
                    Log.w("getCaloriesOverall", "caloriesOverallParse: " + caloriesOverallParse.status)
                    Log.w("getCaloriesOverall", "walksOverallParse: " + walksOverallParse.status)
                    Log.w("getCaloriesOverall", "trainingsParse: " + trainingsParse.status)

                    requestStatus.postValue(walksOverallParse.status)
                }else{
                    var updateTrainingCalories = 0
                    if (trainingsParse.data != null) {
                        for (training in trainingsParse.data!!){
                            if (training.Type=="Cycling"){
                                updateTrainingCalories += training.CaloriesBurn
                            }
                        }
                    }
                    caloriesOverallParse.data!!.Burn = caloriesOverallParse.data!!.Burn + walksOverallParse.data!!.Calories + updateTrainingCalories

                    // Refresh UI Update data
                    caloriesAll.postValue(caloriesOverallParse.data)
                    activityCalories.postValue(walksOverallParse.data!!.Calories + updateTrainingCalories)
                }
            }
        }
    }

    fun updateCaloriesOverall(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if(token != null){
                repository.updateCaloriesOverall(token) { resStatus ->
                    if (resStatus != 200) {
                        Log.w("getCaloriesOverall", "updateCaloriesOverall: $resStatus")
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun getCalories(date: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val caloriesParse = token?.let { repository.getCalories(it,date) }

            if (caloriesParse != null) {
                if (caloriesParse.status != 200){
                    requestStatus.postValue(caloriesParse.status)
                }else{
                    calories.postValue(caloriesParse.data as MutableList<Calories>?)
                }
            }
        }
    }

    fun getCaloriesInfo(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val caloriesParse = token?.let { repository.getCaloriesInfo(it,id) }

            // Refresh UI Update data
            if (caloriesParse != null) {
                if (caloriesParse.status != 200){
                    requestStatus.postValue(caloriesParse.status)
                }else{
                    caloriesInfo.postValue(caloriesParse.data?.get(0))
                }
            }
        }
    }

    fun addCalories(type:String,title:String,content:String,calories:Int,time:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.addCalories(token,type,title,content,calories,time) { resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editCaloriesType(id: Int,type: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesType(token, id , type) { resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editCaloriesTitle(id: Int,title: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesTitle(token, id , title) { resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editCaloriesContent(id: Int,content: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesContent(token, id , content) { resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editCaloriesCalories(id: Int,calories: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesCalories(token, id , calories) { resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editCaloriesTime(id: Int,time: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editCaloriesTime(token, id , time) { resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun deleteCalories(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.deleteCalories(token, id) { resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
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