package com.example.healthExpert.view.period

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityPeriodBinding
import com.google.android.material.snackbar.Snackbar


class Period : AppCompatActivity() {
    private lateinit var binding: ActivityPeriodBinding


    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Period::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.updateBtn.setOnClickListener(View.OnClickListener { view ->
            val snackbar = Snackbar
                .make(binding.root, "Period data updated", Snackbar.LENGTH_LONG)
            snackbar.show()
        })

        binding.backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}