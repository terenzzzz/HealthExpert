package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.CaloriesViewModelFactory
import com.example.healthExpert.viewmodels.MedicationsViewModel
import com.example.healthExpert.viewmodels.MedicationsViewModelFactory

open class MedicationsCompatActivity: AppCompatActivity() {
    // Instantiate the ViewModel from the ImageViewModelFactory
    // which extends ViewModelProvider.Factory
    protected val medicationsViewModel: MedicationsViewModel by viewModels {
        MedicationsViewModelFactory(this)
    }
}