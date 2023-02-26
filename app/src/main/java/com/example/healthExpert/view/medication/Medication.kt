package com.example.healthExpert.view.medication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.MedicationsCompatActivity
import com.example.healthExpert.databinding.ActivityMedicationBinding
import com.example.healthExpert.utils.DateTimeConvert
import java.util.Date
import kotlin.math.log


class Medication : MedicationsCompatActivity() {
    private lateinit var binding: ActivityMedicationBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Medication::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicationsViewModel.medications.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                for (item in list){
                    Log.d("Medications", item.Name)
                }
            }
        })

        binding.settingBtn.setOnClickListener(View.OnClickListener { view ->
            MedicationSetting.startFn(this)
        })


        binding.backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }

    override fun onResume() {
        super.onResume()
        medicationsViewModel.medications(DateTimeConvert().toDate(Date()))


    }
}