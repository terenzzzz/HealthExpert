package com.example.healthExpert.view.medication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.MedicationsCompatActivity
import com.example.healthExpert.databinding.ActivityMedicationEditBinding
import com.example.healthExpert.utils.DatePickerFragment
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.TimePickerFragment
import java.text.SimpleDateFormat

class MedicationEdit : MedicationsCompatActivity() {
    private lateinit var binding: ActivityMedicationEditBinding
    private var initType:String? = null
    private var id:String = ""

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, MedicationEdit::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicationsViewModel.medication.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                initType = item.Type
                setTypeDefault(item.Type)
                binding.etName.hint = item.Name
                binding.etDose.hint = item.Dose.toString()
                binding.etDate.hint = DateTimeConvert().toDate(item.Date)
                binding.etTime.hint = DateTimeConvert().toHHmm(item.Date)

            }
        })

        val extras = intent.extras
        id = extras?.getString("id").toString()
        if (id != "") {
            medicationsViewModel.medication(id)
        }

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.deleteBtn.setOnClickListener (View.OnClickListener { view ->
            medicationsViewModel.deleteMedication(id)
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })


        binding.updateBtn.setOnClickListener (View.OnClickListener { view ->
            val type = binding.etType.selectedItem.toString()
            val name = binding.etName.text.toString()
            val dose = binding.etDose.text.toString()
            val date = binding.etDate.text.toString()
            val time = binding.etTime.text.toString()

            if (type != initType ){
                medicationsViewModel.editMedicationType(id,type)
            }
            if (name!=""){
                medicationsViewModel.editMedicationName(id,name)
            }
            if (dose!=""){
                medicationsViewModel.editMedicationDose(id,dose)
            }
            if (date!=""){
                val time = DateTimeConvert().toTime(medicationsViewModel.medication.value?.Date!!)
                medicationsViewModel.editMedicationDate(id,"$date $time")
            }
            if (time!=""){
                val date = DateTimeConvert().toDate(medicationsViewModel.medication.value?.Date!!)
                medicationsViewModel.editMedicationDate(id,"$date $time")
            }

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

    fun setTypeDefault(type:String){
        val adapter = ArrayAdapter.createFromResource(this, R.array.medicineClass, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etType.adapter = adapter

        val defaultValuePosition = adapter.getPosition(type)
        binding.etType.setSelection(defaultValuePosition)
    }
}