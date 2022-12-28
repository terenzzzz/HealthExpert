package com.example.healthExpert.view.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.databinding.ActivitySignupBinding
import com.example.healthExpert.repository.LoginRepository
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.viewmodels.LoginViewModel
import com.example.healthExpert.viewmodels.SignupViewModel
import com.google.android.material.snackbar.Snackbar

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

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
        signupViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignupViewModel(this@Signup) as T
            }
        })[SignupViewModel::class.java]

        // Check Signup
        signupViewModel.signupStatus.observe(this) { data ->
            Log.d("Signup", "onCreate: $data")
            if (data == 0){
                Snackbar.make(binding.root, "Signup Successfully!", Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(binding.root, "Signup Fail!", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.signUpBtn.setOnClickListener (View.OnClickListener { view ->
            signupViewModel.signup(binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPasswordConfirm.text.toString())
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            Login.startFn(this)
        })
    }
}