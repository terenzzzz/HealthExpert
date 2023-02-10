package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.CaloriesViewModel
import com.example.healthExpert.viewmodels.CaloriesViewModelFactory

open class CaloriesCompatActivity: AppCompatActivity() {

    // Instantiate the ViewModel from the ImageViewModelFactory
    // which extends ViewModelProvider.Factory
    protected val caloriesViewModel: CaloriesViewModel by viewModels {
        CaloriesViewModelFactory(this)
    }
}