package com.example.healthExpert.view.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivitySettingBinding
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
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        binding.userViewModel = userViewModel
        setContentView(binding.root)

        binding.lifecycleOwner = this
        userViewModel.getUserInfo()

        binding.updateBtn.setOnClickListener (View.OnClickListener { view ->
            userViewModel.editName(binding.etName.text.toString())
            userViewModel.editAge(binding.etAge.text.toString())
            userViewModel.editHeight(binding.etHeight.text.toString())
            userViewModel.editWeight(binding.etWeight.text.toString())
            Snackbar.make(view, "Profile Updated", Snackbar.LENGTH_SHORT).show()
        })


        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

    }
}