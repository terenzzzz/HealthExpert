package com.example.healthExpert.view.calories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.CaloriesCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.widget.Ring
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer;

class Calories : CaloriesCompatActivity() {
    private lateinit var binding: ActivityCaloriesBinding
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
        Log.w("Calories", "onCreate: ")
        binding = ActivityCaloriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById (R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        caloriesViewModel.calories.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                Log.w("Calories", "Set Adapter Update UI", )
                recyclerView.adapter = MyAdapter(caloriesViewModel.calories)
            }
        })

        // Set ring
        ringSetUp(binding.calories)


        binding.settingBtn.setOnClickListener (View.OnClickListener { view ->
            CaloriesSetting.startFn(this)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            CaloriesAdd.startFn(this)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }

    override fun onResume() {
        super.onResume()
        Log.w("Calories", "onResume", )
        caloriesViewModel.getCalories()
    }

    private fun ringSetUp(view: View){
        val ring = view.findViewById<Ring>(R.id.calories)
        var calories = 65
        ring.setSweepValue(calories.toFloat())
        ring.setValueText("1139")
        ring.setUnit("KCAL LEFT")
        ring.setBgColor(Color.argb(20,0, 0, 0))
        ring.setSweepColor(Color.rgb(0, 0, 0))
    }
}



// RecycleView Adapter
class MyAdapter(private val caloriesSet: MutableLiveData<MutableList<com.example.healthExpert.model.Calories>?>) : RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var title: TextView = itemView.findViewById(R.id.title)
        var content: TextView = itemView.findViewById(R.id.content)
        var calories: TextView = itemView.findViewById(R.id.calories)
        var time: TextView = itemView.findViewById(R.id.time)
        var editBtn: ImageView = itemView.findViewById(R.id.edit_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_calories_record,
            parent,false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener { view ->
            Log.w("Calories", caloriesSet.value!![position].id.toString() )
        })
        if (caloriesSet.value != null){
            if (caloriesSet.value!![position] != null){
                if (caloriesSet.value!![position].Type == "Intake"){
                    holder.calories.text = "+ "+caloriesSet.value!![position].Calories.toString()
                    holder.icon.setImageResource(R.drawable.meal)

                }else{
                    holder.calories.text = "- "+caloriesSet.value!![position].Calories.toString()
                    holder.icon.setImageResource(R.drawable.calories)
                }
                holder.title.text = caloriesSet.value!![position].Title
                holder.content.text = caloriesSet.value!![position].Content
                holder.time.text = SimpleDateFormat("HH:mm").format(caloriesSet.value!![position].Time)
            }
        }

    }

    override fun getItemCount()= caloriesSet.value?.size ?:0
}