package com.example.healthExpert.viewmodels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.repository.LoginRepository
import com.example.healthExpert.repository.SignupRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    var signupRepo = SignupRepository(activity)
    var signupStatus: LiveData<Int> = MutableLiveData()

    fun signup(email:String, password:String,confirmPassword:String){
        viewModelScope.launch {
            signupRepo.signup(email,password,confirmPassword)
        }
    }
}