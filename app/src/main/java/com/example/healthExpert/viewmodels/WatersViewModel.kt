package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Calories
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
    var watersInfo = MutableLiveData<MutableList<Water>?>()
    var totalWater = MutableLiveData<Int>()
    // TODO combine goal and calculate
    var rate = MutableLiveData<Int>()


    fun getWaters(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getWaters(it) }

            // Refresh UI Update data
            waters.postValue(updatedData)
        }
    }

    fun getWatersInfo(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            Log.w("Calories", "Called: getCaloriesInfo")
            val updatedData = token?.let { repository.getWatersInfo(it,id) }

            // Refresh UI Update data
            watersInfo.postValue(updatedData)
        }
    }

    fun addWaters(type:String,title:String,content:String,value:Int,time:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.addWaters(token,type,title,content,value,time)
            }
        }
    }

    fun editWatersType(id: Int,type: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersType(token, id , type)
            }
        }
    }

    fun editWatersTitle(id: Int,title: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersTitle(token, id , title)
            }
        }
    }

    fun editWatersContent(id: Int,content: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersContent(token, id , content)
            }
        }
    }

    fun editWatersValue(id: Int,value: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersValue(token, id , value)
            }
        }
    }

    fun editWatersTime(id: Int,time: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersTime(token, id , time)
            }
        }
    }

    fun deleteWaters(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.deleteWaters(token, id)
            }
        }
    }
    fun calcDashboard(){
        var totalWater = 0
        for (item in waters.value!!){
            totalWater += item.Value
        }
        this.rate.value = totalWater.div(80)
        this.totalWater.value = totalWater
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