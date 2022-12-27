package com.example.healthExpert.view.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityLoginBinding
import com.example.healthExpert.repository.UserRepository
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.resetPwd.ResetPwd
import com.example.healthExpert.view.signup.Signup
import com.example.healthExpert.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel



    companion object {
        var loginActivity: Login? = null
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
        loginActivity = this

        // Get UserViewModel
        val userRepo = UserRepository.getInstance(this)
        userViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(userRepo) as T
            }
        }).get(UserViewModel::class.java)

        Log.d("Login", "init userViewModel: $userViewModel")

        // Retrieve Token from SharedPreferences
        val sharedPreferences = getSharedPreferences("healthy_expert", MODE_PRIVATE)
        val email = sharedPreferences.getString("email","")
        val password = sharedPreferences.getString("password","")
        if (email != ""){
            binding.etEmail.text = Editable.Factory.getInstance().newEditable(email)
        }
        if (password != ""){
            binding.etPassword.text = Editable.Factory.getInstance().newEditable(password)
        }



        binding.logInBtn.setOnClickListener (View.OnClickListener { view ->
            Log.d("Login", "Loginbtn: Clicked")
            Log.d("Login", "userViewModel: $userViewModel")
            userViewModel.login(binding.etEmail.text.toString(),binding.etPassword.text.toString())
        })

        // Check login
        userViewModel.loginStatus.observe(this) { data ->
            Log.d("Login", "onCreate: $data")
            if (data == 0){
                // 保存账号密码到本地SharedPreferences
                sharedPreferences.edit()
                    .putString("email", binding.etEmail.text.toString())
                    .putString("password", binding.etPassword.text.toString())
                    .commit()
//                Snackbar.make(binding.root, "Log in Successfully!", Snackbar.LENGTH_LONG).show()
                Home.startFn(this)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }else{
                Snackbar.make(binding.root, "Log in Fail!", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.signUpBtn.setOnClickListener (View.OnClickListener { view ->
            Signup.startFn(this)
        })

        binding.resetBtn.setOnClickListener (View.OnClickListener { view ->
            ResetPwd.startFn(this)
        })
    }
}