package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.HistoryViewModel
import com.example.healthExpert.viewmodels.HistoryViewModelFactory
import com.example.healthExpert.viewmodels.UserViewModel
import com.example.healthExpert.viewmodels.UserViewModelFactory

open class UserCompatActivity: AppCompatActivity() {

    // Instantiate the ViewModel from the Factory
    // which extends ViewModelProvider.Factory
    protected val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(this)
    }
}