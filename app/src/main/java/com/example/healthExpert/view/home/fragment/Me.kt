package com.example.login.view.homePage.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.healthExpert.R
import com.example.healthExpert.databinding.FragmentMeBinding
import com.example.healthExpert.view.setting.Setting
import com.example.healthExpert.viewmodels.LoginViewModel


class Me : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
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


        val button = view.findViewById<ImageView>(R.id.setting_btn)
        button.setOnClickListener{
            this.context?.let { it1 -> Setting.startFn(it1) }
        }

        return view

    }
}
