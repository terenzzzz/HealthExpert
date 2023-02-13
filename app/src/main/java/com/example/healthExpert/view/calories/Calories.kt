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
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.CaloriesCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.widget.Ring
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer;
import com.example.healthExpert.widget.RingView

class Calories : CaloriesCompatActivity() {
    private lateinit var binding: ActivityCaloriesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var ring: Ring

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
        binding.lifecycleOwner = this
        binding.caloriesViewmodel = caloriesViewModel
        setContentView(binding.root)

        recyclerView = findViewById (R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)


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
        // Init ring
        ring = ringSetUp(binding.calories)
        ring.setValueText("Outside")


        caloriesViewModel.calories.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                caloriesViewModel.calcDashboard()
                ring.setValueText(caloriesViewModel.totalCalories.value.toString())
                ring.setSweepValue(caloriesViewModel.totalCalories.value!!.times(100).div(1000f) ?: 0f)
                recyclerView.adapter = MyAdapter(caloriesViewModel.calories,this)
            }

        })
        caloriesViewModel.getCalories()


    }

    fun ringSetUp(view: View): Ring {
        val ring = view.findViewById<Ring>(R.id.calories)
        ring.setSweepValue(0f)
        ring.setValueText("0")
        ring.setUnit("KCAL To Limit")
        ring.setBgColor(Color.argb(20,0, 0, 0))
        ring.setSweepColor(Color.rgb(255, 205, 105))
        return ring
    }

}



// RecycleView Adapter
class MyAdapter(private val caloriesSet: MutableLiveData<MutableList<com.example.healthExpert.model.Calories>?>,private val activity:Context) : RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var title: TextView = itemView.findViewById(R.id.title)
        var content: TextView = itemView.findViewById(R.id.content)
        var calories: TextView = itemView.findViewById(R.id.calories)
        var time: TextView = itemView.findViewById(R.id.time)
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
            val intent = Intent(activity, CaloriesEdit::class.java)
            val bundle = Bundle()
            bundle.putInt("id", caloriesSet.value!![position].id)
            intent.putExtras(bundle)
            activity.startActivity(intent)
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