package com.example.healthExpert.view.medication

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.MedicationsCompatActivity
import com.example.healthExpert.databinding.ActivityMedicationAddBinding
import com.example.healthExpert.utils.DatePickerFragment
import com.example.healthExpert.utils.TimePickerFragment

class MedicationAdd : MedicationsCompatActivity() {
    private lateinit var binding: ActivityMedicationAddBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, MedicationAdd::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            val type = binding.etType.selectedItem.toString()
            val name = binding.etName.text.toString()
            val dose = binding.etDose.text.toString()
            val dateTime = "${binding.etDate.text} ${binding.etTime.text}"
            medicationsViewModel.addMedication(type,name,dose,dateTime)
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }

    fun showDatePickerDialog(v: View) {
        DatePickerFragment(binding.etDate).show(supportFragmentManager, "datePicker")
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment(binding.etTime).show(supportFragmentManager, "timePicker")
    }
}