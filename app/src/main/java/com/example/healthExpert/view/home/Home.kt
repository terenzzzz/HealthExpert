package com.example.healthExpert.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityHomeBinding
import com.example.healthExpert.view.login.Login
import com.example.login.view.homePage.fragment.History
import com.example.login.view.homePage.fragment.Me
import com.example.login.view.homePage.fragment.Overall
import com.example.login.view.homePage.fragment.Sources
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Home : AppCompatActivity() {
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
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


}