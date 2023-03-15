package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.*
import com.example.healthExpert.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverallViewModel(private val fragment: Fragment) : ViewModel()  {

    private val sharedPreferences: SharedPreferences =
        fragment.requireActivity().getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    private val caloriesRepository = CaloriesRepository()
    private val walkRepository = WalkRepository()
    private val watersRepository = WatersRepository()
    private val trainingsRepository = TrainingsRepository()
    private val medicationRepository = MedicationRepository()
    private val sleepRepository = SleepRepository()
    private val userRepository = UserRepository()


    var user = MutableLiveData<User?>()
    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { userRepository.getUserInfo(it) }

            // Refresh UI Update data
            user.postValue(updatedData)

        }
    }

    var caloriesAll = MutableLiveData<CaloriesOverall?>()
    fun getCaloriesOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                caloriesRepository.updateCaloriesOverall(token)
                val updatedData = caloriesRepository.getCaloriesOverall(token,date)
                // Refresh UI Update data
                caloriesAll.postValue(updatedData)
            }
        }
    }

    var walkAll = MutableLiveData<WalksOverall?>()
    fun getWalksOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository

            val updatedData = token?.let { walkRepository.getWalksOverall(it,date) }

            // Refresh UI Update data
            walkAll.postValue(updatedData)
        }
    }

    var watersAll = MutableLiveData<WaterOverall?>()
    fun getWatersOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                watersRepository.updateWaterOverall(token)
                val updatedData =watersRepository.getWaterOverall(token,date)
                // Refresh UI Update data
                watersAll.postValue(updatedData)
            }
        }
    }

    var trainingAll = MutableLiveData<TrainingOverall?>()
    fun getTrainingOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                trainingsRepository.updateWaterOverall(token)
                val updatedData = trainingsRepository.getTrainingOverall(token,date)

                // Refresh UI Update data
                trainingAll.postValue(updatedData)
            }

        }
    }

    var medications = MutableLiveData<MutableList<Medication>?>()
    fun medications(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { medicationRepository.medications(it,date) }

            // Refresh UI Update data
            medications.postValue(updatedData)
        }
    }

    var sleep = MutableLiveData<Sleep?>()
    fun getSleep(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { sleepRepository.getSleep(it) }

            // Refresh UI Update data
            sleep.postValue(updatedData)
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