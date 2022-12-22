package com.example.healthExpert.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityLoginBinding
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.resetPwd.ResetPwd
import com.example.healthExpert.view.signup.Signup

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Login::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logInBtn.setOnClickListener (View.OnClickListener { view ->
            Home.startFn(this)
        })

        binding.signUpBtn.setOnClickListener (View.OnClickListener { view ->
            Signup.startFn(this)
        })

        binding.resetBtn.setOnClickListener (View.OnClickListener { view ->
            ResetPwd.startFn(this)
        })
    }
}