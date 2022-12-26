package com.example.healthExpert.view.resetPwd

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.databinding.ActivityResetPwdBinding
import com.example.healthExpert.repository.UserRepository
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class ResetPwd : AppCompatActivity() {
    private lateinit var binding: ActivityResetPwdBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, ResetPwd::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetBtn.setOnClickListener (View.OnClickListener { view ->
            Login.startFn(this)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            Login.startFn(this)
        })
    }
}