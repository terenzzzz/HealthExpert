package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Water
import com.example.healthExpert.model.WaterOverall
import com.example.healthExpert.repository.WatersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatersViewModel(private val activity: AppCompatActivity) : ViewModel() {
    var requestStatus = MutableLiveData<Int>()
    private val repository = WatersRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var waters = MutableLiveData<MutableList<Water>?>()
    var watersInfo = MutableLiveData<MutableList<Water>?>()
    var watersAll = MutableLiveData<WaterOverall?>()

    fun getWatersOverall(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.updateWaterOverall(token)
                val waterOverallParse =repository.getWaterOverall(token,date)
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

    fun updateWatersOverall(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if(token != null){
                repository.updateWaterOverall(token)
            }
        }
    }


    fun getWaters(date: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val waterParse = token?.let { repository.getWaters(it,date) }

            // Refresh UI Update data
            if (waterParse != null) {
                if (waterParse.status != 200){
                    requestStatus.postValue(waterParse.status)
                }
                waters.postValue(waterParse.data as MutableList<Water>?)
            }
        }
    }

    fun getWatersInfo(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val waterParse = token?.let { repository.getWatersInfo(it,id) }

            // Refresh UI Update data
            if (waterParse != null) {
                if (waterParse.status != 200){
                    requestStatus.postValue(waterParse.status)
                }
                watersInfo.postValue(waterParse.data as MutableList<Water>?)
            }
        }
    }

    fun addWaters(type:String,title:String,content:String,value:Int,time:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.addWaters(token,type,title,content,value,time){ resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editWatersType(id: Int,type: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersType(token, id , type){ resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editWatersTitle(id: Int,title: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersTitle(token, id , title){ resStatus ->
                    if (resStatus != 200) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editWatersContent(id: Int,content: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersContent(token, id , content){ resStatus ->
                    if (resStatus != 0) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editWatersValue(id: Int,value: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersValue(token, id , value){ resStatus ->
                    if (resStatus != 0) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun editWatersTime(id: Int,time: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.editWatersTime(token, id , time){ resStatus ->
                    if (resStatus != 0) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
        }
    }

    fun deleteWaters(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null) {
                repository.deleteWaters(token, id){ resStatus ->
                    if (resStatus != 0) {
                        requestStatus.postValue(resStatus as Int?)
                    }
                }
            }
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