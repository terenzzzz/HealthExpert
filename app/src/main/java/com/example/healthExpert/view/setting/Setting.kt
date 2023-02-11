package com.example.healthExpert.view.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivitySettingBinding
import com.example.healthExpert.model.User
import com.example.healthExpert.repository.CaloriesRepository
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
        })


        // Change Password Button
        binding.changePasswordBtn.setOnClickListener( View.OnClickListener {
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
        userViewModel.getUserInfo()

        userViewModel.user.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                if (item.Gender == "Male"){
                    binding.avatar.setImageResource(R.drawable.avatar)
                }else{
                    binding.avatar.setImageResource(R.drawable.hannah)
                }
            }
        })
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