package com.example.healthExpert.view.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivitySettingBinding
import com.example.healthExpert.model.User
import com.google.android.material.snackbar.Snackbar
import kotlin.math.round

class Setting : UserCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Setting::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w("Setting", "onCreate: ")
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.userViewModel = userViewModel

        setContentView(binding.root)

        // Update Button
        binding.updateBtn.setOnClickListener (View.OnClickListener { view ->
            userViewModel.editName(binding.etName.text.toString())
            userViewModel.editGender(binding.etGender.selectedItem.toString())
            if (binding.etAge.text.toString()!=""){
                userViewModel.editAge(Integer.parseInt(binding.etAge.text.toString()))
            }
            if (binding.etHeight.text.toString()!=""){
                userViewModel.editHeight(binding.etHeight.text.toString().toFloat())
            }
            if (binding.etWeight.text.toString()!=""){
                userViewModel.editWeight(binding.etWeight.text.toString().toFloat())
            }
            Snackbar.make(view, "Profile Updated", Snackbar.LENGTH_SHORT).show()
        })


        // Change Password Button
        binding.changePasswordBtn.setOnClickListener( View.OnClickListener {
            Log.w("Setting", userViewModel.calcBMI(53f,173f).toString())
        })

        // Help Button
        binding.helpBtn.setOnClickListener( View.OnClickListener { view ->
//            Log.w("setting", "Gender: "+binding.etGender.selectedItem)
//            Log.w("setting", "Name: "+userViewModel.user.value!!.Name)
//            Log.w("setting", "Age: "+userViewModel.user.value!!.Age)
//            Log.w("setting", "Height: "+userViewModel.user.value!!.Height)
//            Log.w("setting", "Weight: "+userViewModel.user.value!!.Weight)
        })

        // Back Button
        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

    }

    override fun onRestart() {
        super.onRestart()
        Log.w("Setting", "onRestart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.w("Setting", "onResume: ")
//        userViewModel.getUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("Setting", "onDestroy: ")
    }

    override fun onStop() {
        super.onStop()
        Log.w("Setting", "onStop: ")
    }
}