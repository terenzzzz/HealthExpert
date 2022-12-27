package com.example.login.view.homePage.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityHomeBinding
import com.example.healthExpert.databinding.FragmentMeBinding
import com.example.healthExpert.repository.UserRepository
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.view.setting.Setting
import com.example.healthExpert.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar


class Me : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentMeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMeBinding.inflate(layoutInflater)
        val view = binding.root

        // Get UserViewModel
        val userRepo = Login.loginActivity?.let { UserRepository.getInstance(it) }
        userViewModel = Login.loginActivity?.let {
            ViewModelProvider(it, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return userRepo?.let { it1 -> UserViewModel(it1) } as T
                }
            }).get(UserViewModel::class.java)
        }!!
        binding.userViewModel =userViewModel
        


        val button = view.findViewById<ImageView>(R.id.setting_btn)
        button.setOnClickListener{
            this.context?.let { it1 -> Setting.startFn(it1) }
        }

        return view

    }
}
