package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.*
import com.example.healthExpert.parse.TrainingOverallParse
import com.example.healthExpert.parse.UserInfoParse
import com.example.healthExpert.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverallViewModel(private val fragment: Fragment) : ViewModel()  {
    var requestStatus = MutableLiveData<Int>()

    private val sharedPreferences: SharedPreferences =
        fragment.requireActivity().getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    private val height = sharedPreferences.getFloat("height",0f)
    private val weight = sharedPreferences.getFloat("weight",0f)
    private val caloriesRepository = CaloriesRepository()
    private val goalsRepository = GoalsRepository()
    private val walkRepository = WalkRepository()
    private val watersRepository = WatersRepository()
    private val trainingsRepository = TrainingsRepository()
    private val medicationRepository = MedicationRepository()
    private val sleepRepository = SleepRepository()

    var goals = MutableLiveData<Goal>()
    fun getGoals(){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val goalParse = goalsRepository.getGoal(token)
                goals.postValue(goalParse.data)
            }
        }
    }

    var caloriesAll = MutableLiveData<CaloriesOverall?>()
    fun getCaloriesOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                caloriesRepository.updateCaloriesOverall(token) { resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
                val caloriesOverallParse = caloriesRepository.getCaloriesOverall(token,date)
                if (caloriesOverallParse != null) {
                    if (caloriesOverallParse.status != 200){
                        requestStatus.postValue(caloriesOverallParse.status)
                    }
                    caloriesAll.postValue(caloriesOverallParse.data)
                }
            }
        }
    }

    var walkAll = MutableLiveData<WalksOverall?>()
    fun getWalksOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                walkRepository.updateWalksOverall(token,height,weight)
                val walksOverallParse = walkRepository.getWalksOverall(token,date)
                // Refresh UI Update data
                if (walksOverallParse != null) {
                    if (walksOverallParse.status != 200){
                        requestStatus.postValue(walksOverallParse.status)
                    }
                    walkAll.postValue(walksOverallParse.data)
                }
            }

        }
    }

    var watersAll = MutableLiveData<WaterOverall?>()
    fun getWatersOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                watersRepository.updateWaterOverall(token)
                val waterOverallParse =watersRepository.getWaterOverall(token,date)
                // Refresh UI Update data
                if (waterOverallParse != null) {
                    if (waterOverallParse.status != 200){
                        requestStatus.postValue(waterOverallParse.status)
                    }
                    watersAll.postValue(waterOverallParse.data)
                }
            }
        }
    }

    var trainingAll = MutableLiveData<TrainingOverall?>()
    fun getTrainingOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                trainingsRepository.updateWaterOverall(token)
                val trainingOverallParse = trainingsRepository.getTrainingOverall(token,date)

                // Refresh UI Update data
                if (trainingOverallParse != null) {
                    if (trainingOverallParse.status != 200){
                        requestStatus.postValue(trainingOverallParse.status)
                    }
                    trainingAll.postValue(trainingOverallParse.data)
                }
            }

        }
    }

    var medications = MutableLiveData<MutableList<Medication>?>()
    fun medications(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val medicationsParse = token?.let { medicationRepository.medications(it,date) }
            // Refresh UI Update data
            if (medicationsParse != null) {
                if (medicationsParse.status != 200){
                    requestStatus.postValue(medicationsParse.status)
                }
                medications.postValue(medicationsParse.data as MutableList<Medication>?)
            }
        }
    }

    var sleep = MutableLiveData<Sleep?>()
    fun getSleep(date: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val sleepParse = token?.let { sleepRepository.getSleep(it,date) }

            // Refresh UI Update data
            if (sleepParse != null) {
                if (sleepParse.status != 200){
                    requestStatus.postValue(sleepParse.status)
                }
                sleep.postValue(sleepParse.data)
            }
        }
    }

}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class OverallViewModelFactory(private val activity: Fragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverallViewModel::class.java)) {
            return OverallViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}