package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.healthExpert.model.User
import com.example.healthExpert.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    private val repository = UserRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var user = MutableLiveData<User?>(null)
    var test = MutableLiveData<String>("123")


    fun getUserInfo(){
        viewModelScope.launch {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getUserInfo(it) }

            // notify the UI to refresh and show the updated data
//            user.value = updatedData
            // TODO: Not Updating UI Properly
            user.postValue(updatedData)
        }
    }

    fun editName(name:String){
        viewModelScope.launch {
            if (token != null) {
                repository.editName(token,name)
            }
        }
    }

    fun editAge(age:String){
        viewModelScope.launch {
            if (token != null) {
                repository.editAge(token,age)
            }
        }
    }

    fun editWeight(weight:String){
        viewModelScope.launch {
            if (token != null) {
                repository.editWeight(token,weight)
            }
        }
    }

    fun editHeight(height:String){
        viewModelScope.launch {
            if (token != null) {
                repository.editHeight(token,height)
            }
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