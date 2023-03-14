package com.example.login.view.homePage.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.healthExpert.R
import com.example.healthExpert.databinding.FragmentMeBinding
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.setting.Setting
import com.example.healthExpert.viewmodels.UserViewModel


class Me : Fragment() {
    private lateinit var binding: FragmentMeBinding
    private lateinit var userViewModel:UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMeBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = this
        userViewModel.getUserInfo()
        val view = binding.root

        userViewModel.user.observe(requireActivity(), Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                if (item.Gender == "Male"){
                    binding.avatar.setImageResource(R.drawable.avatar)
                }else{
                    binding.avatar.setImageResource(R.drawable.hannah)
                }
            }else{
                SnackbarUtil().buildNetwork(binding.root)
            }
        })


        val button = view.findViewById<ImageView>(R.id.setting_btn)
        button.setOnClickListener{
            this.context?.let { it1 -> Setting.startFn(it1) }
        }

        return view
    }
}
