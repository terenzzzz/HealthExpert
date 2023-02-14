package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.healthExpert.compatActivity.HistoryCompatFragment
import com.example.healthExpert.repository.UserRepository

class HistoryViewModel(private val fragment: Fragment) : ViewModel()  {
    private val repository = UserRepository()

    private val sharedPreferences: SharedPreferences =
        fragment.requireActivity().getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")





}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class HistoryViewModelFactory(private val activity: Fragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}