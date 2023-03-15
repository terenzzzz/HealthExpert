package com.example.healthExpert.view.onBoarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityHomeBinding
import com.example.healthExpert.databinding.ActivityOnboardingBinding
import com.example.healthExpert.databinding.ActivityWalkBinding
import com.example.healthExpert.view.home.ViewPagerAdapter
import com.example.healthExpert.view.home.fragment.History
import com.example.healthExpert.view.home.fragment.Sources
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.view.onBoarding.fragment.OnBoarding1
import com.example.healthExpert.view.onBoarding.fragment.OnBoarding2
import com.example.healthExpert.view.onBoarding.fragment.OnBoarding3
import com.example.healthExpert.view.onBoarding.fragment.OnBoarding4
import com.example.healthExpert.view.walk.Walk
import com.example.login.view.homePage.fragment.Me
import com.example.login.view.homePage.fragment.Overall
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnBoarding : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var vp:ViewPager2
    private var list: MutableList<Fragment> = ArrayList()

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, OnBoarding::class.java)
            context.startActivity(intent)
//            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vp = binding.vpViewpager


        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Get the current page index
                Log.d("MyActivity", "Current page index: $position")
                setUnSelected()
                setSelected(position)
            }
        }

        initPage(onPageChangeCallback)

        binding.nextBtn.setOnClickListener {
            if (vp.currentItem<list.size-1){
                vp.currentItem = vp.currentItem + 1
            }else{
                val sharedPreferences = getSharedPreferences("healthy_expert", MODE_PRIVATE)
                sharedPreferences.edit()
                    .putBoolean("onBoarded",true)
                    .commit()
                Login.startFn(this)
                finish()
            }
        }

        binding.skipBtn.setOnClickListener {
            Login.startFn(this)
        }
    }



    private fun initPage(onPageChangeCallback:ViewPager2.OnPageChangeCallback){
        // Instant
        val onBoarding1 = OnBoarding1()
        val onBoarding2 = OnBoarding2()
        val onBoarding3 = OnBoarding3()
        val onBoarding4 = OnBoarding4()

        list.add(onBoarding1)
        list.add(onBoarding2)
        list.add(onBoarding3)
        list.add(onBoarding4)

        // Get view
        val adapter = ViewPagerAdapter(this,list)

//
//        // Set adapter to VP
        vp.adapter = adapter
        vp.registerOnPageChangeCallback(onPageChangeCallback)
//


    }

    private fun setUnSelected(){
        binding.firstBtn.background = ContextCompat.getDrawable(this@OnBoarding, R.drawable.ob_unselected_radio)
        binding.secondBtn.background = ContextCompat.getDrawable(this@OnBoarding, R.drawable.ob_unselected_radio)
        binding.thirdBtn.background = ContextCompat.getDrawable(this@OnBoarding, R.drawable.ob_unselected_radio)
        binding.fourthBtn.background = ContextCompat.getDrawable(this@OnBoarding, R.drawable.ob_unselected_radio)
    }

    private fun setSelected(position: Int){
        when (position){
            0 ->  {
                binding.firstBtn.width = 30
                binding.firstBtn.background = ContextCompat.getDrawable(this@OnBoarding, R.drawable.ob_selected_radio)
            }
            1 ->  {
                binding.firstBtn.width = 30
                binding.secondBtn.background = ContextCompat.getDrawable(this@OnBoarding, R.drawable.ob_selected_radio)
            }

            2 ->  {
                binding.firstBtn.width = 30
                binding.thirdBtn.background = ContextCompat.getDrawable(this@OnBoarding, R.drawable.ob_selected_radio)
            }
            3 ->  {
                binding.firstBtn.width = 30
                binding.fourthBtn.background = ContextCompat.getDrawable(this@OnBoarding, R.drawable.ob_selected_radio)
            }
        }

    }


}

class OnBoardingVPAdapter(activity: FragmentActivity, val list: List<Fragment>): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}