package com.example.healthExpert.view.sidebar


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.R

import com.example.healthExpert.databinding.ActivitySidebarBinding
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.view.setting.Setting


class Sidebar : AppCompatActivity() {
    private lateinit var binding: ActivitySidebarBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Sidebar::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySidebarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menu.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        binding.homeBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        binding.profileBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        binding.historyBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        binding.sourcesBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        binding.supportBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        binding.settingBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            Setting.startFn(this)
        })

        binding.sourcesBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        binding.logOutBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            // Clear SharedPreferences
            val sharedPreferences: SharedPreferences =
                this.getSharedPreferences("healthy_expert", MODE_PRIVATE)
            sharedPreferences.edit()
                .clear()
                .commit()
            Login.startFn(this)
        })
    }

}