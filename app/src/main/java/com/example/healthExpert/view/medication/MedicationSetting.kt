package com.example.healthExpert.view.medication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.MedicationsCompatActivity
import com.example.healthExpert.databinding.ActivityMedicationSettingBinding

class MedicationSetting : MedicationsCompatActivity() {
    private lateinit var binding: ActivityMedicationSettingBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, MedicationSetting::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById (R.id.recycler_view)
        layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

//        recyclerView.adapter = MedicationsAdapter(medicationsViewModel.medications,this)



        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.saveBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}