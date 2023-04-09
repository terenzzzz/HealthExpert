package com.example.healthExpert.view.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivityInitSettingBinding
import com.example.healthExpert.repository.GoalsRepository
import com.google.android.material.snackbar.Snackbar

class InitSetting : UserCompatActivity() {
    private lateinit var binding: ActivityInitSettingBinding
    private val repository = GoalsRepository()

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

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
        val token = sharedPreferences.getString("token","")
        if (token != null) {
            repository.initGoals(token)
        }

        // Update Button
        binding.nextBtn.setOnClickListener { view ->

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
        }
    }

    private fun checkEmpty(string: String): Boolean {
        return string != ""
    }

}