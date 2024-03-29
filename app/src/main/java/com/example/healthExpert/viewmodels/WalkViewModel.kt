package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.WalkStep
import com.example.healthExpert.model.WalksOverall
import com.example.healthExpert.repository.WalkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class WalkViewModel(private val activity: AppCompatActivity) : ViewModel() {
    var requestStatus = MutableLiveData<Int>()

    private val repository = WalkRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    private val height = sharedPreferences.getFloat("height",0f)
    private val weight = sharedPreferences.getFloat("weight",0f)


    var walkAll = MutableLiveData<WalksOverall?>()
    var walkSteps = MutableLiveData<MutableList<WalkStep>?>()


    fun getWalksOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.updateWalksOverall(token,height,weight)
                val walksOverallParse = repository.getWalksOverall(token,date)
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

    fun getWalkSteps(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val walkStepsParse = token?.let { repository.getWalkStep(it,date) }
            if (walkStepsParse != null) {
                if (walkStepsParse.status != 200){
                    requestStatus.postValue(walkStepsParse.status)
                }
                walkSteps.postValue(walkStepsParse.data as MutableList<WalkStep>?)
            }
        }
    }

    fun updateWalksOverall(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.updateWalksOverall(it,weight,height) }
        }
    }

}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class WalkViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalkViewModel::class.java)) {
            return WalkViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}