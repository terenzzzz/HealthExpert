package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.Goal
import com.example.healthExpert.model.HeartRate
import com.example.healthExpert.model.News
import com.example.healthExpert.parse.HeartRateParse
import com.example.healthExpert.repository.CaloriesRepository
import com.example.healthExpert.repository.HeartRateRepository
import com.example.healthExpert.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeartRateViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    var requestStatus = MutableLiveData<Int>()
    private val repository = HeartRateRepository()

    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")

    var bpm = MutableLiveData<String>()
    var heartRates = MutableLiveData<MutableList<HeartRate>?>()

    fun setBpm(bpm:String){
        this.bpm.postValue(bpm)
    }

    fun getHeartRates(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null){
                // 获取卡路里汇总数据
                val heartRateParse = repository.getHeartRates(token,date)
                if (heartRateParse.status!=200){
                    requestStatus.postValue(heartRateParse.status)
                }else{
                    heartRates.postValue(heartRateParse.data as MutableList<HeartRate>?)
                }
            }
        }
    }

    fun addHeartRate(heartRate:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            if (token != null){
                // 获取卡路里汇总数据
                val reqStatus = repository.addHeartRate(token,heartRate)
                if (reqStatus != 200){
                    requestStatus.postValue(reqStatus)
                }
            }
        }
    }



}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class HeartRateViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeartRateViewModel::class.java)) {
            return HeartRateViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}