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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.CaloriesCompatActivity
import com.example.healthExpert.databinding.ActivityCaloriesBinding
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.widget.Ring
import java.text.SimpleDateFormat
import java.util.*

class Calories : CaloriesCompatActivity() {
    private lateinit var binding: ActivityCaloriesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var ring: Ring
    private lateinit var layoutManager: LinearLayoutManager
    private var todayDate = DateTimeConvert.toDate(Date())
    var mode = "edit"

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
        layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager


        caloriesViewModel.caloriesAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update Ring the UI
                var intake = item.Intake
                var burn = item.Burn
                var total = burn?.let { intake?.minus(it) }
                var rate = total?.div(10f)
                ring.setValueText(total.toString())
                if (rate != null) {
                    ring.setSweepValue(rate.toFloat())
                }
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })

        caloriesViewModel.activityCalories.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                binding.activityValue.text = "- $item"
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })


        caloriesViewModel.calories.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                caloriesViewModel.updateCaloriesOverall()
                recyclerView.adapter = CaloriesAdapter(caloriesViewModel.calories,this,mode)
                caloriesViewModel.getCaloriesOverall(todayDate)
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })

        val bundle = intent.extras
        if (bundle != null && bundle.getString("selectedDate") != "") {
            todayDate = bundle.getString("selectedDate").toString()
            mode = "view"
            binding.addBtn.visibility = View.GONE
            binding.settingBtn.visibility = View.GONE
        }


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
        Log.w("Calories", "onResume")
        // Init ring
        ring = ringSetUp(binding.calories)

        caloriesViewModel.getCaloriesOverall(todayDate)
        caloriesViewModel.getCalories(todayDate)
    }

    fun ringSetUp(view: View): Ring {
        val ring = view.findViewById<Ring>(R.id.calories)
        ring.setSweepValue(0f)
        ring.setValueText("0")
        ring.setUnit("KCAL To Limit")
        ring.setStateText("Active")
        ring.setBgColor(Color.argb(20,0, 0, 0))
        ring.setSweepColor(Color.rgb(255, 205, 105))
        return ring
    }
}



// RecycleView Adapter
class CaloriesAdapter(private val caloriesSet: MutableLiveData<MutableList<com.example.healthExpert.model.Calories>?>,
                      private val activity:Context,private val mode:String) : RecyclerView.Adapter<CaloriesAdapter.ViewHolder>(){

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
        if(mode!="edit"){
                holder.editBtn.visibility = View.GONE
        }else{
            holder.itemView.setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(activity, CaloriesEdit::class.java)
                val bundle = Bundle()
                bundle.putInt("id", caloriesSet.value!![position].id)
                intent.putExtras(bundle)
                activity.startActivity(intent)
            })
        }

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