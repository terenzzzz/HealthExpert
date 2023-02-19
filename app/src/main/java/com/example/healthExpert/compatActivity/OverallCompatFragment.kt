package com.example.healthExpert.compatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.healthExpert.viewmodels.OverallViewModel
import com.example.healthExpert.viewmodels.OverallViewModelFactory

open class OverallCompatFragment: Fragment() {
    protected val overallViewModel: OverallViewModel by viewModels {
        OverallViewModelFactory(this)
    }
}