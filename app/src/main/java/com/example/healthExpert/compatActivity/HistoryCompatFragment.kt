package com.example.healthExpert.compatActivity

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.example.healthExpert.viewmodels.HistoryViewModel
import com.example.healthExpert.viewmodels.HistoryViewModelFactory

open class HistoryCompatFragment: Fragment() {
    protected val historyViewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory(this)
    }
}