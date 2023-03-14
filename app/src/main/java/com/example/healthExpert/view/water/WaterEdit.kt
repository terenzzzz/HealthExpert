package com.example.healthExpert.view.water

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.CaloriesCompatActivity
import com.example.healthExpert.compatActivity.WatersCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesEditBinding
import com.example.healthExpert.databinding.ActivityWatersEditBinding
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.utils.TimePickerFragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Formatter


class WaterEdit : WatersCompatActivity() {
    private lateinit var binding: ActivityWatersEditBinding
    private lateinit var selectedType:String
    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("CaloriesEdit", "onResume: ")
        super.onCreate(savedInstanceState)
        binding = ActivityWatersEditBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.watersViewmodel = watersViewModel
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

        binding.deleteBtn.setOnClickListener (View.OnClickListener { view ->
            watersViewModel.deleteWaters(id)
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.updateBtn.setOnClickListener (View.OnClickListener { view ->
//            Log.d("CaloriesEdit", watersViewModel.caloriesInfo.value.toString())

            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val value = binding.etValue.text.toString()
            val time = binding.etTime.text.toString()
            if (selectedType != watersViewModel.watersInfo.value?.get(0)?.Type){
                watersViewModel.editWatersType(id,selectedType!!)
            }
            if (title!="" && title != watersViewModel.watersInfo.value?.get(0)?.Title){
                watersViewModel.editWatersTitle(id,title)
            }
            if (content!="" && content != watersViewModel.watersInfo.value?.get(0)?.Content){
                watersViewModel.editWatersContent(id,content)
            }
            if (value !="" && value != watersViewModel.watersInfo.value?.get(0)?.Value.toString()){
                watersViewModel.editWatersValue(id,value)
            }
            if (time != SimpleDateFormat("HH:mm").format(watersViewModel.watersInfo.value?.get(0)?.Time)){
                watersViewModel.editWatersTime(id,time)
            }
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

    }

    override fun onResume() {
        Log.d("CaloriesEdit", "onResume: ")
        super.onResume()

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getInt("id")
            watersViewModel.getWatersInfo(id)
        }

        watersViewModel.watersInfo.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                when(watersViewModel.watersInfo.value?.get(0)?.Type){
                    "Coffee" -> setSelectedCard("Coffee",binding.cardA)
                    "EnergyDrink" -> setSelectedCard("EnergyDrink",binding.cardB)
                    "Water" -> setSelectedCard("Water",binding.cardC)
                    "ColdDrink" -> setSelectedCard("ColdDrink",binding.cardD)
                    "BubbleTea" -> setSelectedCard("BubbleTea",binding.cardE)
                    "Beer" -> setSelectedCard("Beer",binding.cardF)
                }
            }else{
                SnackbarUtil().buildNetwork(binding.root)
            }
        })
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment(binding.etTime).show(supportFragmentManager, "timePicker")
    }

    private fun  setSelectedCard(type: String, view: View) {
        selectedType = type
        binding.cardA.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardB.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardC.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardD.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardE.background = getDrawable(R.drawable.radius_btn_gray)
        binding.cardF.background = getDrawable(R.drawable.radius_btn_gray)
        view.background = getDrawable(R.drawable.radius_btn_green)
    }
}