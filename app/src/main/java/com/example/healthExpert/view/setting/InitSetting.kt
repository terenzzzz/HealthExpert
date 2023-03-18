package com.example.healthExpert.view.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivityInitSettingBinding
import com.example.healthExpert.databinding.ActivitySettingBinding
import com.example.healthExpert.model.User
import com.example.healthExpert.repository.CaloriesRepository
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.resetPwd.ResetPwd
import com.google.android.material.snackbar.Snackbar
import kotlin.math.round

class InitSetting : UserCompatActivity() {
    private lateinit var binding: ActivityInitSettingBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, InitSetting::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w("Setting", "onCreate: ")
        super.onCreate(savedInstanceState)

        binding = ActivityInitSettingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Update Button
        binding.nextBtn.setOnClickListener (View.OnClickListener { view ->

            val checkName = checkEmpty(binding.etName.text.toString())
            val checkAge = checkEmpty(binding.etAge.text.toString())
            val checkHeight = checkEmpty(binding.etHeight.text.toString())
            val checkWeight = checkEmpty(binding.etWeight.text.toString())



            if(checkName&&checkAge&&checkHeight&&checkWeight){
                userViewModel.initUser(binding.etHeight.text.toString(),binding.etWeight.text.toString(),
                    binding.etAge.text.toString(), binding.etGender.selectedItem as String,binding.etName.text.toString())
                Snackbar.make(view, "Profile Updated", Snackbar.LENGTH_SHORT).show()
                finish()
            }else{
                Snackbar.make(view, "Please fill all the field correctly", Snackbar.LENGTH_SHORT).show()
            }


        })
    }

    private fun checkEmpty(string: String): Boolean {
        return string != ""
    }

}