package com.example.healthExpert.view.help

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthExpert.R

import com.example.healthExpert.databinding.ActivityHelpBinding


class Help : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Help::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
            this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}