package com.example.healthExpert.view.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivitySettingBinding
import kotlin.math.log


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

        binding.updateBtn.setOnClickListener (View.OnClickListener {
//            Log.d("Setting", "updateBtn: clicked ")
//            Log.d("Setting", userViewModel.user.value!!.Name)
//            Log.d("Setting", userViewModel.user.value!!.Age.toString())
        })


        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

    }
}