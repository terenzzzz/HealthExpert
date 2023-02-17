package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.WalkStep
import com.example.healthExpert.model.Walks
import com.example.healthExpert.repository.WalkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class WalkViewModel(private val activity: AppCompatActivity) : ViewModel() {
    private val repository = WalkRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var walk = MutableLiveData<Walks?>()
    var walkSteps = MutableLiveData<MutableList<WalkStep>?>()


    fun getWalks(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getWalks(it) }

            // Refresh UI Update data
            walk.postValue(updatedData)
        }
    }

    fun getWalkSteps(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getWalkStep(it) }

            // Refresh UI Update data
            walkSteps.postValue(updatedData)
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