package com.example.healthExpert.compatActivity


import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.SleepViewModel
import com.example.healthExpert.viewmodels.SleepViewModelFactory

open class SleepCompatActivity: AppCompatActivity() {

    // Instantiate the ViewModel from the ImageViewModelFactory
    // which extends ViewModelProvider.Factory
    protected val sleepViewModel: SleepViewModel by viewModels {
        SleepViewModelFactory(this)
    }
}