package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.TrainingViewModel
import com.example.healthExpert.viewmodels.TrainingViewModelFactory

open class TrainingsCompatActivity : AppCompatActivity() {
    // Instantiate the ViewModel from the ImageViewModelFactory
    // which extends ViewModelProvider.Factory
    protected val trainingsViewModel: TrainingViewModel by viewModels {
        TrainingViewModelFactory(this)
    }
}