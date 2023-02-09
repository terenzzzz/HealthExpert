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

    init {
        getUserInfo()
    }


    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            Log.w("viewmodel", "getUserInfo: called")
            val updatedData = token?.let { repository.getUserInfo(it) }

            // notify the UI to refresh and show the updated data
            // TODO: Not Updating UI Properly
            user.postValue(updatedData)
            test.postValue("456")
            Log.w("viewmodel", "postValue: called")
        }
    }

    fun editName(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                repository.editName(token,name)
            }
        }
    }

    fun editGender(gender:String){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                repository.editGender(token,gender)
            }
        }
    }

    fun editAge(age:Int){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                repository.editAge(token,age)
            }
        }
    }

    fun editWeight(weight:Float){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                repository.editWeight(token,weight)
            }
        }
    }

    fun editHeight(height:Float){
        viewModelScope.launch(Dispatchers.IO) {
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