package com.example.healthExpert.compatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.healthExpert.viewmodels.SourcesViewModel
import com.example.healthExpert.viewmodels.SourcesViewModelFactory

open class SourcesCompatFragment: Fragment() {
    protected val sourcesViewModel: SourcesViewModel by viewModels {
        SourcesViewModelFactory(this)
    }
}