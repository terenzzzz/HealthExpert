package com.example.healthExpert.view.story

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.databinding.ActivityStoryBinding
import com.example.healthExpert.view.calories.Calories

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var recyclerView: RecyclerView

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Calories::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}