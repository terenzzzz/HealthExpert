package com.example.healthExpert.view.resetPwd

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivityResetPwdBinding
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.login.Login

class ResetPwd : UserCompatActivity() {
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

        binding.changeBtn.setOnClickListener {
            userViewModel.changePassword(binding.oldPassword.text.toString(),binding.newPassword.text.toString())
            finish()
        }

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            Login.startFn(this)
        })
    }
}