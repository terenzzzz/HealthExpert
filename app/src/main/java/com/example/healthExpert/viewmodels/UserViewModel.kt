package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.User
import com.example.healthExpert.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    private val repository = UserRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    val user = MutableLiveData<User?>()

    fun getUser(){
        viewModelScope.launch {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getUserInfo(it) }

            // notify the UI to refresh and show the updated data
            user.postValue(updatedData)
        }
    }

}