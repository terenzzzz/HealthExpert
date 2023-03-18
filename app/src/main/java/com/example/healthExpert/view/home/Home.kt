package com.example.healthExpert.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.UserCompatActivity
import com.example.healthExpert.databinding.ActivityHomeBinding
import com.example.healthExpert.service.LocationService
import com.example.healthExpert.service.NotificationService
import com.example.healthExpert.service.StepService
import com.example.healthExpert.view.setting.Setting
import com.example.healthExpert.view.sidebar.Sidebar
import com.example.healthExpert.view.home.fragment.History
import com.example.login.view.homePage.fragment.Me
import com.example.login.view.homePage.fragment.Overall
import com.example.healthExpert.view.home.fragment.Sources
import com.example.healthExpert.view.setting.InitSetting
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class Home : UserCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Home::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel.user.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // 检测用户有没有填写信息
                if (item.Name.isNullOrEmpty() || item.Height.equals(0F) || item.Weight.equals(0F)){
                    InitSetting.startFn(this)
                }


                // Update the UI
                if (item.Gender == "Male"){
                    binding.avatar.setImageResource(R.drawable.avatar)
                }else{
                    binding.avatar.setImageResource(R.drawable.hannah)
                }
                binding.greating.text = "Hi, ${item.Name}"
                val sharedPreferences = getSharedPreferences("healthy_expert", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putFloat("height", item.Height)
                editor.putFloat("weight", item.Weight)
                editor.apply()
            }
        })
        userViewModel.getUserInfo()



        Log.w("Home", "onCreate: ")
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.userViewmodel = userViewModel
        setContentView(binding.root)
        initPage()

        callLocationService()
        callNotificationService()




        binding.sideBar.setOnClickListener (View.OnClickListener {
            Sidebar.startFn(this)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
        })

        binding.avatar.setOnClickListener (View.OnClickListener {
            Setting.startFn(this)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        })

    }


    override fun onResume() {
        super.onResume()
        Log.w("Home", "onResume: ")

        userViewModel.getUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("Home", "onDestroy: ")
    }

    override fun onStop() {
        super.onStop()
        Log.w("Home", "onStop: ")
    }

    /**
     * function to call the Location Service to start
     */
    private fun callLocationService(){
        Log.d("Home", "callService: ")
        Intent(this, StepService::class.java).apply {
            startService(this)
        }
    }

    /**
     * function to call the Location Service to start
     */
    private fun callNotificationService(){
        Log.d("通知服务", "call: ")
        Intent(this, NotificationService::class.java).apply {
            startService(this)
        }
    }

    /**
     * function to stop Service when is no longer needed
     */
    private fun stopService(){
        Intent(this, LocationService::class.java).apply {
            stopService(this)
        }

    }

    private fun initFragment(): MutableList<Fragment> {
        val overall = Overall()
        val history = History()
        val sources = Sources()
        val me = Me()
        val list: MutableList<Fragment> = ArrayList()
        list.add(overall)
        list.add(history)
        list.add(sources)
        list.add(me)
        return list
    }

    private fun initTab(): Array<String> {
        val tabs = arrayOf("OVERALL", "HISTORY", "SOURCES", "ME")
        return tabs
    }

    private fun initTabIcon(): Array<Int> {
        return arrayOf(R.drawable.home, R.drawable.calendar, R.drawable.global, R.drawable.user)
    }

    private fun initPage(){
        // Instant
        val fragments = initFragment()
        val tabs = initTab()
        val tabIcons = initTabIcon()

        // Get view
        val vp = findViewById<ViewPager2>(R.id.vp_viewpager)
        val tabLayout = findViewById<TabLayout>(R.id.tl_tabLayout)
        val adapter = ViewPagerAdapter(this,fragments)

        // Set adapter to VP
        vp.adapter = adapter



//        // Link vp to TabLayout
        TabLayoutMediator(tabLayout, vp) { tab, position ->
            tab.text = tabs[position]
            val tabView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            tabView.findViewById<ImageView>(R.id.tab_icon).setImageResource(tabIcons[position])
//            tabView.findViewById<TextView>(R.id.tab_text).text = tabs[position]
            tab.customView = tabView
        }.attach()
    }

}