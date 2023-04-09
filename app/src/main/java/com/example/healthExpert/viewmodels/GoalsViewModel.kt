package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.Goal
import com.example.healthExpert.repository.CaloriesRepository
import com.example.healthExpert.repository.GoalsRepository
import com.example.healthExpert.repository.TrainingsRepository
import com.example.healthExpert.repository.WalkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalsViewModel(private val activity: AppCompatActivity) : ViewModel() {
    var requestStatus = MutableLiveData<Int?>()

    private val repository = GoalsRepository()

    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var goals = MutableLiveData<Goal>()


    fun getGoals(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val goalParse = token?.let { repository.getGoal(it) }

            if (goalParse != null) {
                if (goalParse.status != 200){
                    requestStatus.postValue(goalParse.status)
                }else{
                    goals.postValue(goalParse.data)
                }
            }
        }
    }

    fun editCalories(calories:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val resStatus = token?.let { repository.editCalories(it, calories) }
            if (resStatus != 200) {
                requestStatus.postValue(resStatus)
            }
        }
    }

    fun editSteps(calories:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val resStatus = token?.let { repository.editSteps(it, calories) }
            if (resStatus != 200) {
                requestStatus.postValue(resStatus)
            }
        }
    }

    fun editTraining(calories:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val resStatus = token?.let { repository.editTraining(it, calories) }
            if (resStatus != 200) {
                requestStatus.postValue(resStatus)
            }
        }
    }

    fun editWater(calories:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val resStatus = token?.let { repository.editWater(it, calories) }
            if (resStatus != 200) {
                requestStatus.postValue(resStatus)
            }
        }
    }

    fun editSleep(calories:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val resStatus = token?.let { repository.editSleep(it, calories) }
            if (resStatus != 200) {
                requestStatus.postValue(resStatus)
            }
        }
    }

}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class GoalsViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalsViewModel::class.java)) {
            return GoalsViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}