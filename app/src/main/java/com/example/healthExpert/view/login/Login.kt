package com.example.healthExpert.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.databinding.ActivityLoginBinding
import com.example.healthExpert.repository.UserRepository
import com.example.healthExpert.view.resetPwd.ResetPwd
import com.example.healthExpert.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

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

        // Get UserViewModel
        val userRepo = UserRepository(this)
        userViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(userRepo) as T
            }
        }).get(UserViewModel::class.java)

        // Check login
        userViewModel.loginStatus.observe(this) { data ->
            Log.d("Login", "onCreate: $data")
            if (data == 0){
                Snackbar.make(binding.root, "Log in Successfully!", Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(binding.root, "Log in Fail!", Snackbar.LENGTH_LONG).show()
            }
        }



        binding.logInBtn.setOnClickListener (View.OnClickListener { view ->
            userViewModel.login(binding.etEmail.text.toString(),binding.etPassword.text.toString())


//            Log.d("LoginA", "token: $token")
//            Home.startFn(this)
        })

        binding.signUpBtn.setOnClickListener (View.OnClickListener { view ->
            userViewModel.getUser(3)

//            Log.d("Login", "token: $token")
//            Signup.startFn(this)
        })

        binding.resetBtn.setOnClickListener (View.OnClickListener { view ->
            ResetPwd.startFn(this)
        })
    }
}