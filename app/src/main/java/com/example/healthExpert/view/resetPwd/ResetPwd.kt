package com.example.healthExpert.view.resetPwd

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivityResetPwdBinding
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.login.Login
import com.google.android.material.snackbar.Snackbar

class ResetPwd : UserCompatActivity() {
    private lateinit var binding: ActivityResetPwdBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, ResetPwd::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code != null){
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })

        userViewModel.passwordStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code != null ){
                if (code != 200){
                    SnackbarUtil.buildPassword(binding.root,code)
                }else{
                    finish()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            }
        })

        binding.changeBtn.setOnClickListener {
            val oldPsw = binding.oldPassword.text.toString()
            val newPwd = binding.newPassword.text.toString()
            if (oldPsw!="" && newPwd!=""){
                if (oldPsw == newPwd){
                    Snackbar.make(binding.root, "New Password Can Not The Same As The Old One", Snackbar.LENGTH_SHORT).show()
                }else{
                    userViewModel.changePassword(binding.oldPassword.text.toString(),binding.newPassword.text.toString())
                }
            }else{
                Snackbar.make(binding.root, "Please Fill In The Fields", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}