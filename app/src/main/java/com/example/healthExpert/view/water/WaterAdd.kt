package com.example.healthExpert.view.water

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.WatersCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesAddBinding
import com.example.healthExpert.databinding.ActivityWaterAddBinding
import com.example.healthExpert.utils.TimePickerFragment
import com.google.android.material.snackbar.Snackbar

class WaterAdd : WatersCompatActivity() {
    private lateinit var binding: ActivityWaterAddBinding
    private lateinit var selectedType:String

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, WaterAdd::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaterAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Todo: Need to change default border color
        binding.cardA.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardB.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardC.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardD.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardE.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardF.background = getDrawable(R.drawable.radius_btn_gray)

        binding.cardA.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("Coffee",view)
        })
        binding.cardB.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("EnergyDrink",view)
        })
        binding.cardC.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("Water",view)
        })

        binding.cardD.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("ColdDrink",view)
        })
        binding.cardE.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("BubbleTea",view)
        })
        binding.cardF.setOnClickListener(View.OnClickListener { view ->
            setSelectedCard("Beer",view)
        })


        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val value = binding.etValue.text.toString()
            val time = binding.etTime.text.toString()
            if(selectedType.isNullOrEmpty() || title.isNullOrEmpty() || content.isNullOrEmpty() ||
                value.isNullOrEmpty() || time.isNullOrEmpty()){
                Snackbar.make(binding.root, "Please fill in all the field", Snackbar.LENGTH_LONG).show()
            }else{
                watersViewModel.addWaters(selectedType!!,title, content,Integer.parseInt(value), time)
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })
    }

    private fun  setSelectedCard(type: String, view: View) {
        selectedType = type
        binding.cardA.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardB.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardC.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardD.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardE.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardF.background = getDrawable(R.drawable.radius_btn_gray)
        view.background = view.context.getDrawable(R.drawable.radius_btn_green)
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment(binding.etTime).show(supportFragmentManager, "timePicker")
    }
}