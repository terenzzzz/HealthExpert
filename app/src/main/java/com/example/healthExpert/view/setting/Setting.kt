package com.example.healthExpert.view.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityLoginBinding
import com.example.healthExpert.databinding.ActivitySettingBinding
import com.example.healthExpert.view.login.Login

class Setting : AppCompatActivity() {
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
        setContentView(binding.root)

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

    }
}