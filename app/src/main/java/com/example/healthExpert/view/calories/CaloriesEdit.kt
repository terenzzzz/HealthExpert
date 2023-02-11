package com.example.healthExpert.view.calories

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.CaloriesCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesEditBinding
import com.example.healthExpert.utils.TimePickerFragment
import com.google.android.material.snackbar.Snackbar


class CaloriesEdit : CaloriesCompatActivity() {
    private lateinit var binding: ActivityCaloriesEditBinding
    private var selectedType:String? = null
    private var id:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("CaloriesEdit", "onResume: ")
        super.onCreate(savedInstanceState)
        binding = ActivityCaloriesEditBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.caloriesViewmodels = caloriesViewModel
        setContentView(binding.root)


        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getInt("id")
            Log.d("CaloriesEdit", "id: $id")
        }

        binding.intakeBtn.setOnClickListener(View.OnClickListener { view ->
            selectedType = binding.intakeType.text.toString()
            view.background = this.getDrawable(R.drawable.radius_btn_green)
            binding.burnBtn.background = this.getDrawable(R.drawable.radius_btn_gray)

        })

        binding.burnBtn.setOnClickListener(View.OnClickListener { view ->
            selectedType = binding.burnType.text.toString()
            view.background = this.getDrawable(R.drawable.radius_btn_green)
            binding.intakeBtn.background = this.getDrawable(R.drawable.radius_btn_gray)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.updateBtn.setOnClickListener (View.OnClickListener { view ->
            Log.d("CaloriesEdit", caloriesViewModel.caloriesInfo.value.toString())
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val calories = binding.etCalories.text.toString()
            val time = binding.etTime.text.toString()
//            if(title.isNullOrEmpty() && content.isNullOrEmpty() && calories.isNullOrEmpty()){
//                Snackbar.make(binding.root, "Please fill in all the field", Snackbar.LENGTH_LONG).show()
//            }else{
//                caloriesViewModel.addCalories(selectedType!!,title, content,Integer.parseInt(calories), time)
//                finish()
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//            }
        })

    }

    override fun onResume() {
        Log.d("CaloriesEdit", "onResume: ")
        super.onResume()
        caloriesViewModel.getCaloriesInfo(id)

        caloriesViewModel.caloriesInfo.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                if (caloriesViewModel.caloriesInfo.value?.Type ?: "" == "Intake"){
                    binding.intakeBtn.background = this.getDrawable(R.drawable.radius_btn_green)
                }else{
                    binding.burnBtn.background = this.getDrawable(R.drawable.radius_btn_green)
                }
            }
        })
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment(binding.etTime).show(supportFragmentManager, "timePicker")
    }
}