package com.example.healthExpert.view.calories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.CaloriesCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesAddBinding
import com.example.healthExpert.utils.TimePickerFragment
import com.google.android.material.snackbar.Snackbar

class CaloriesAdd : CaloriesCompatActivity() {
    private lateinit var binding: ActivityCaloriesAddBinding
    private var selectedType:String? = null

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, CaloriesAdd::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaloriesAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val calories = binding.etCalories.text.toString()
            val time = binding.etTime.text.toString()
            if(selectedType.isNullOrEmpty() || title.isNullOrEmpty() || content.isNullOrEmpty() ||
                calories.isNullOrEmpty() || time.isNullOrEmpty()){
                Snackbar.make(binding.root, "Please fill in all the field", Snackbar.LENGTH_LONG).show()
            }else{
                caloriesViewModel.addCalories(selectedType!!,title, content,Integer.parseInt(calories), time)
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment(binding.etTime).show(supportFragmentManager, "timePicker")
    }
}

