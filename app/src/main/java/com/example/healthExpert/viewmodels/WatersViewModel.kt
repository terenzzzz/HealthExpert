package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Water
import com.example.healthExpert.repository.WatersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatersViewModel(private val activity: AppCompatActivity) : ViewModel() {
    private val repository = WatersRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var waters = MutableLiveData<MutableList<Water>?>()


    fun getWaters(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getWaters(it) }

            // Refresh UI Update data
            waters.postValue(updatedData)
        }
    }


}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class WatersViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatersViewModel::class.java)) {
            return WatersViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}