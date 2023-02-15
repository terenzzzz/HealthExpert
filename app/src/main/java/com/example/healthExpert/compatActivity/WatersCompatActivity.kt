package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.viewmodels.WatersViewModel
import com.example.healthExpert.viewmodels.WatersViewModelFactory


open class WatersCompatActivity: AppCompatActivity() {

    // Instantiate the ViewModel from the ImageViewModelFactory
    // which extends ViewModelProvider.Factory
    protected val watersViewModel: WatersViewModel by viewModels {
        WatersViewModelFactory(this)
    }
}