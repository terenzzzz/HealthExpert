package com.example.healthExpert.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepository) : ViewModel() {
    // ViewModel code goes here
    var loginStatus: LiveData<Int> = MutableLiveData()
    var signupStatus: LiveData<Int> = MutableLiveData()


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

}