package com.example.healthExpert.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.healthExpert.model.User
import com.example.healthExpert.repository.UserRepository
import kotlinx.coroutines.launch
import kotlin.math.log
import kotlin.math.pow

class UserViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    private val repository = UserRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var user = MutableLiveData<User?>(null)


    fun getUserInfo(){
        viewModelScope.launch {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getUserInfo(it) }

            // notify the UI to refresh and show the updated data
            user.postValue(updatedData)
        }
    }

}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class UserViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}