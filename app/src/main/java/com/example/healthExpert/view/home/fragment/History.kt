package com.example.healthExpert.view.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthExpert.compatActivity.HistoryCompatFragment
import com.example.healthExpert.databinding.FragmentHistoryBinding
import com.example.healthExpert.utils.DatePickerFragment
import com.example.healthExpert.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.logging.SimpleFormatter


class History : HistoryCompatFragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var userViewModel:UserViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.historyViewmodel = historyViewModel

        binding.etDate.text = SimpleDateFormat("yyyy-MM-dd").format(Date())

        binding.etDate.setOnClickListener (View.OnClickListener {
            showDatePickerDialog(binding.root)
        })



        return binding.root
    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment(binding.etDate)
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }
}

