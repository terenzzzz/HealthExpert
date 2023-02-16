package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.WalkViewModel
import com.example.healthExpert.viewmodels.WalkViewModelFactory

open class WalkCompatActivity: AppCompatActivity() {

    // Instantiate the ViewModel from the Factory
    // which extends ViewModelProvider.Factory
    protected val walkViewModel: WalkViewModel by viewModels {
        WalkViewModelFactory(this)
    }
}