package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.CaloriesViewModel
import com.example.healthExpert.viewmodels.CaloriesViewModelFactory
import com.example.healthExpert.viewmodels.HeartRateViewModel
import com.example.healthExpert.viewmodels.HeartRateViewModelFactory

open class HeartRateCompatActivity: AppCompatActivity() {

    // Instantiate the ViewModel from the ImageViewModelFactory
    // which extends ViewModelProvider.Factory
    protected val heartRateViewModel: HeartRateViewModel by viewModels {
        HeartRateViewModelFactory(this)
    }
}