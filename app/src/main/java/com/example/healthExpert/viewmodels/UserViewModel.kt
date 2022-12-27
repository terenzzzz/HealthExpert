package com.example.healthExpert.viewmodels


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.healthExpert.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepository) : ViewModel() {
    // ViewModel code goes here
    var signupStatus: LiveData<Int> = MutableLiveData()
    // Log in
    var loginStatus: LiveData<Int> = MutableLiveData()
    var idUser: LiveData<Int> = MutableLiveData()
    // User Info
    var email: LiveData<String> = MutableLiveData()
    var name: LiveData<String> = MutableLiveData()
    var age: LiveData<Int> = MutableLiveData()
    var height: LiveData<Float> = MutableLiveData()
    var weight: LiveData<Float> = MutableLiveData()
    var bmi: LiveData<Float> = MutableLiveData()
    var bodyFat: LiveData<Float> = MutableLiveData()


    fun getUser(idUser:Int){
        viewModelScope.launch {
            userRepo.getUser(idUser)
            Log.d("Login", loginStatus.value.toString())
        }
    }

    fun signup(email:String, password:String, confirmPassword:String){
        viewModelScope.launch {
            userRepo.signup(email,password, confirmPassword)
        }
    }

    fun login(email:String, password:String){
        viewModelScope.launch {
            userRepo.login(email,password)
        }
    }

    fun getUserInfo(idUser:Int){
        viewModelScope.launch {
            userRepo.getUserInfo(idUser)
        }
    }


}
