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

    var caloriesAll = MutableLiveData<CaloriesOverall?>()
    fun getCaloriesOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { caloriesRepository.getCaloriesOverall(it,date) }

            // Refresh UI Update data
            caloriesAll.postValue(updatedData)
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
            val updatedData = token?.let { watersRepository.getWaterOverall(it,date) }

            // Refresh UI Update data
            watersAll.postValue(updatedData)
        }
    }

    var trainingAll = MutableLiveData<TrainingOverall?>()
    fun getTrainingOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { trainingsRepository.getTrainingOverall(it,date) }

            // Refresh UI Update data
            trainingAll.postValue(updatedData)
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