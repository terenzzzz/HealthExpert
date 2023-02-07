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
        Log.d("Setting", "onCreate: ")
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.userViewModel = userViewModel
        setContentView(binding.root)

        binding.updateBtn.setOnClickListener (View.OnClickListener { view ->
            userViewModel.editName(binding.etName.text.toString())
            userViewModel.editAge(binding.etAge.text.toString())
            userViewModel.editHeight(binding.etHeight.text.toString())
            userViewModel.editWeight(binding.etWeight.text.toString())
            Snackbar.make(view, "Profile Updated", Snackbar.LENGTH_SHORT).show()
        })

        binding.changePasswordBtn.setOnClickListener( View.OnClickListener {
            Log.d("Setting", "changePasswordBtn: clicked")
            userViewModel.getUserInfo()
        })

        binding.helpBtn.setOnClickListener( View.OnClickListener { view ->
            Log.d("setting", "Name: "+userViewModel.user.value!!.Name)
            Log.d("setting", "Age: "+userViewModel.user.value!!.Age)
            Log.d("setting", "Height: "+userViewModel.user.value!!.Height)
            Log.d("setting", "Weight: "+userViewModel.user.value!!.Weight)
        })


        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Setting", "onRestart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Setting", "onResume: ")
        userViewModel.getUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Setting", "onDestroy: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Setting", "onStop: ")
    }
}