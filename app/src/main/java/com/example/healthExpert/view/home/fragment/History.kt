package com.example.healthExpert.view.home.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.example.healthExpert.compatActivity.HistoryCompatFragment
import com.example.healthExpert.databinding.FragmentHistoryBinding
import com.example.healthExpert.utils.DatePickerFragment
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.logging.SimpleFormatter
import kotlin.math.log


class History : HistoryCompatFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentHistoryBinding
    private var selectedDate = DateTimeConvert().toDate(Date())


    override fun onResume() {
        super.onResume()
        historyViewModel.getCaloriesOverall(selectedDate)
        historyViewModel.getWalksOverall(selectedDate)
        historyViewModel.getWatersOverall(selectedDate)
        historyViewModel.getTrainingOverall(selectedDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.historyViewmodel = historyViewModel

        binding.etDate.text = selectedDate

        binding.etDate.setOnClickListener (View.OnClickListener {
            showDatePicker()
        })


        return binding.root
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        dialog.show()
    }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        var newMonth = month + 1
        var newMonthStr = newMonth.toString()
        if(newMonth<10){
            newMonthStr = "0$newMonth"
        }
        selectedDate = "$year-$newMonthStr-$day"
        binding.etDate.text = selectedDate

        historyViewModel.getCaloriesOverall(selectedDate)
        historyViewModel.getWalksOverall(selectedDate)
        historyViewModel.getWatersOverall(selectedDate)
        historyViewModel.getTrainingOverall(selectedDate)
    }
}

