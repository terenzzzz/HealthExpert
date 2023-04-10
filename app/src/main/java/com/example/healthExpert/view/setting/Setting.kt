package com.example.healthExpert.view.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivitySettingBinding
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.goals.GoalsSetting
import com.example.healthExpert.view.help.Help
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.view.resetPwd.ResetPwd
import com.google.android.material.snackbar.Snackbar

class Setting : UserCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Setting::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w("Setting", "onCreate: ")
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.userViewModel = userViewModel

        setContentView(binding.root)

        userViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code != null){
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })

        userViewModel.user.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                if (item.Gender == "Male"){
                    setGenderDefault("Male")
                    binding.avatar.setImageResource(R.drawable.avatar)
                }else{
                    setGenderDefault("Female")
                    binding.avatar.setImageResource(R.drawable.hannah)
                }
            }
        })

        binding.goalsSettingBtn.setOnClickListener{
            GoalsSetting.startFn(this)
        }

        // Update Button
        binding.updateBtn.setOnClickListener { view ->
            if (binding.etName.text.toString()!=""){
                userViewModel.editName(binding.etName.text.toString())
                binding.etName.text.clear()
            }
            if (binding.etGender.selectedItem.toString()!= userViewModel.user.value?.Gender ?: ""){
                userViewModel.editGender(binding.etGender.selectedItem.toString())
            }
            if (binding.etAge.text.toString()!=""){
                userViewModel.editAge(Integer.parseInt(binding.etAge.text.toString()))
                binding.etAge.text.clear()
            }
            if (binding.etHeight.text.toString()!=""){
                userViewModel.editHeight(binding.etHeight.text.toString().toFloat())
                binding.etHeight.text.clear()
            }
            if (binding.etWeight.text.toString()!=""){
                userViewModel.editWeight(binding.etWeight.text.toString().toFloat())
                binding.etWeight.text.clear()
            }
            Snackbar.make(view, "Profile Updated", Snackbar.LENGTH_SHORT).show()

            userViewModel.getUserInfo()
        }


        // Change Password Button
        binding.changePasswordBtn.setOnClickListener{
            ResetPwd.startFn(this)
        }

        // Help Button
        binding.helpBtn.setOnClickListener {
            Help.startFn(this)
        }

        // Back Button
        binding.backBtn.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.logOutBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            // Clear SharedPreferences
            val sharedPreferences: SharedPreferences =
                this.getSharedPreferences("healthy_expert", MODE_PRIVATE)
            val remember = sharedPreferences.getBoolean("remember",false)
            if (remember){
                sharedPreferences.edit()
                    .remove("token")
                    .commit()
            }else{
                sharedPreferences.edit()
                    .clear()
                    .commit()
            }
            Login.startFn(this)
        }

    }

    override fun onRestart() {
        super.onRestart()
        Log.w("Setting", "onRestart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.w("Setting", "onResume: ")
        userViewModel.getUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("Setting", "onDestroy: ")
    }

    override fun onStop() {
        super.onStop()
        Log.w("Setting", "onStop: ")
    }

    fun setGenderDefault(gender:String){
        val adapter = ArrayAdapter.createFromResource(this, R.array.genderClass, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etGender.adapter = adapter

        val defaultValuePosition = adapter.getPosition(gender)
        binding.etGender.setSelection(defaultValuePosition)
    }
}