package com.example.healthExpert.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityHomeBinding
import com.example.healthExpert.repository.UserRepository
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.view.sidebar.Sidebar
import com.example.healthExpert.viewmodels.UserViewModel
import com.example.login.view.homePage.fragment.History
import com.example.login.view.homePage.fragment.Me
import com.example.login.view.homePage.fragment.Overall
import com.example.login.view.homePage.fragment.Sources
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userViewModel: UserViewModel

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Home::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPage()

        // Get UserViewModel
        val userRepo = UserRepository.getInstance(this)
        userViewModel = Login.loginActivity?.let {
            ViewModelProvider(it, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return UserViewModel(userRepo) as T
                }
            }).get(UserViewModel::class.java)
        }!!
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = this


        // Check user Id
        userViewModel.idUser.observe(this) { data ->
            Log.d("Home", "userId: $data")
            if (data > 0){
                // Get User Info
                userViewModel.idUser.value?.let { userViewModel.getUserInfo(it) }
            }else{
                Snackbar.make(binding.root, "Get Info Fail!", Snackbar.LENGTH_LONG).show()
            }
        }

        userViewModel.name.observe(this) { data ->
            Log.d("Home", "name: $data")
//            binding.greating.text = data
        }











        binding.sideBar.setOnClickListener (View.OnClickListener { view ->
            Sidebar.startFn(this)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
        })

    }

    fun initFragment(): MutableList<Fragment> {
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

    fun initTab(): Array<String> {
        val tabs = arrayOf("OVERALL", "HISTORY", "SOURCES", "ME")
        return tabs
    }

    fun initTabIcon(): Array<Int> {
        val tabIcons = arrayOf(R.drawable.home,R.drawable.calendar, R.drawable.global, R.drawable.user)
        return tabIcons
    }

    fun initPage(){
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

        // Link vp to TabLayout
        TabLayoutMediator(tabLayout, vp) { tab, position ->
            tab.text = tabs[position]
            tab.icon = getDrawable(tabIcons[position])
        }.attach()
    }


}