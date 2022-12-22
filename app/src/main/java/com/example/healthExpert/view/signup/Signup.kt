package com.example.healthExpert.view.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityLoginBinding
import com.example.healthExpert.databinding.ActivitySignupBinding
import com.example.healthExpert.view.login.Login

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Signup::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener (View.OnClickListener { view ->
            Login.startFn(this)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            Login.startFn(this)
        })
    }
}