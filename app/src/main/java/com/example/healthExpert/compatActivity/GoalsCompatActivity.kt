package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.GoalsViewModel
import com.example.healthExpert.viewmodels.GoalsViewModelFactory

open class GoalsCompatActivity: AppCompatActivity() {

    // Instantiate the ViewModel from the ImageViewModelFactory
    // which extends ViewModelProvider.Factory
    protected val goalsViewModel: GoalsViewModel by viewModels {
        GoalsViewModelFactory(this)
    }
}