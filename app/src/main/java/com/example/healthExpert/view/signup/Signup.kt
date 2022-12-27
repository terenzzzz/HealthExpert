package com.example.healthExpert.view.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityLoginBinding
import com.example.healthExpert.databinding.ActivitySignupBinding
import com.example.healthExpert.repository.UserRepository
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userViewModel: UserViewModel

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

        // Get UserViewModel
        val userRepo = UserRepository.getInstance(this)
        userViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(userRepo) as T
            }
        }).get(UserViewModel::class.java)

        // Check Signup
        userViewModel.signupStatus.observe(this) { data ->
            Log.d("Signup", "onCreate: $data")
            if (data == 0){
                Snackbar.make(binding.root, "Signup Successfully!", Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(binding.root, "Signup Fail!", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.signUpBtn.setOnClickListener (View.OnClickListener { view ->
            userViewModel.signup(binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPasswordConfirm.text.toString())
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            Login.startFn(this)
        })
    }
}