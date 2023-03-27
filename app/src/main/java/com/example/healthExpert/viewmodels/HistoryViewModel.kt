package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.healthExpert.compatActivity.HistoryCompatFragment
import com.example.healthExpert.model.*
import com.example.healthExpert.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val fragment: Fragment) : ViewModel()  {
    private val sharedPreferences: SharedPreferences =
        fragment.requireActivity().getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    private val caloriesRepository = CaloriesRepository()
    private val walkRepository = WalkRepository()
    private val watersRepository = WatersRepository()
    private val trainingsRepository = TrainingsRepository()
    private val sleepRepository = SleepRepository()

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

    var sleep = MutableLiveData<Sleep?>()
    fun getSleep(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { sleepRepository.getSleep(it,date) }

            // Refresh UI Update data
            sleep.postValue(updatedData)
        }
    }

}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class HistoryViewModelFactory(private val activity: Fragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}