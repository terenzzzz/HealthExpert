package com.example.healthExpert.view.news

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityNewBinding
import com.example.healthExpert.view.calories.Calories
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class NewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewBinding
    private var id = -1
    private var title = ""
    private var content = ""
    private var date = ""
    private var author = ""
    private var image = ""

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
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getInt("id")
            title = bundle.getString("title").toString()
            content = bundle.getString("content").toString()
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
            date = android.icu.text.SimpleDateFormat("YYYY-MM-dd").format(format.parse(bundle.getString("date")))
            author = bundle.getString("author").toString()
            image = bundle.getString("image").toString()
        }
        binding.title.text = title
        binding.content.text = content
        binding.date.text = date
        binding.author.text = author
        Picasso.get().load(image).into(binding.image)

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}