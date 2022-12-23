package com.example.login.view.homePage.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.healthExpert.R
import com.example.healthExpert.view.setting.Setting


class Me : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_me, container, false)

        val button = view.findViewById<ImageView>(R.id.setting_btn)
        button.setOnClickListener{
            this.context?.let { it1 -> Setting.startFn(it1) }
        }

        return view

    }
}