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
import java.lang.String.format
import java.util.*
import kotlin.math.pow

class UserViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    private val repository = UserRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var user = MutableLiveData<User?>(null)

    init {
        getUserInfo()
    }


    // Todo: Init Gender
    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getUserInfo(it) }

            // Refresh UI Update data
            user.postValue(updatedData)

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

    fun calcBMI(weight: Float,height: Float) : Float{
        return String.format("%.1f",weight.div(height.div(100).pow(2))).toFloat()
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