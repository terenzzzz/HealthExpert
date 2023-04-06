package com.example.healthExpert.view.training

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
import com.example.healthExpert.compatActivity.TrainingsCompatActivity
import com.example.healthExpert.databinding.ActivityTrainBinding
import com.example.healthExpert.model.Trainings
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.calories.CaloriesEdit
import com.example.healthExpert.widget.Ring
import java.text.SimpleDateFormat
import java.util.*

class Train : TrainingsCompatActivity() {
    private lateinit var binding: ActivityTrainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var ring: Ring
    private var todayDate = DateTimeConvert.toDate(Date())


    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Train::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.trainViewmodel = trainingsViewModel
        setContentView(binding.root)


        trainingsViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code != null){
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })

        trainingsViewModel.trainingAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                ring.setValueText(item.Calories.toString())
                ring.setSweepValue(item.Calories.times(100).div(1000f))
            }
        })

        trainingsViewModel.trainings.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                trainingsViewModel.updateTrainingOverall()
                recyclerView.adapter = TrainingsAdapter(
                    trainingsViewModel.trainings, this
                )
            }
        })

        val bundle = intent.extras
        if (bundle != null && bundle.getString("selectedDate") != "") {
            todayDate = bundle.getString("selectedDate").toString()
            binding.addBtn.visibility = View.GONE
            binding.settingBtn.visibility = View.GONE
        }

        recyclerView = binding.recyclerView
        layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager


        binding.settingBtn.setOnClickListener (View.OnClickListener { view ->
            TrainSetting.startFn(this)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            TrainAdd.startFn(this)
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

        trainingsViewModel.getTrainingOverall(todayDate)
        trainingsViewModel.getTrainings(todayDate)
    }

    private fun ringSetUp(view: View): Ring {
        val ring = view.findViewById<Ring>(R.id.calories)
        ring.setSweepValue(0f)
        ring.setValueText("0")
        ring.setStateText("Active")
        ring.setUnit("kcal")
        ring.setBgColor(Color.argb(20,0, 0, 0))
        ring.setSweepColor(Color.rgb(234, 67, 53))
        return ring
    }
}

// RecycleView Adapter
class TrainingsAdapter(private val trainingsSet: MutableLiveData<MutableList<Trainings>?>,
                       private val activity:Context) : RecyclerView.Adapter<TrainingsAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var time: TextView = itemView.findViewById(R.id.time)
        var type: ImageView = itemView.findViewById(R.id.type)
        var title: TextView = itemView.findViewById(R.id.title)
        var distance: TextView = itemView.findViewById(R.id.distance)
        var speed: TextView = itemView.findViewById(R.id.speed)
        var calories: TextView = itemView.findViewById(R.id.calories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_training_record,
            parent,false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener { view ->
            Log.w("trainingsSet", trainingsSet.value!![position].id.toString() )
            val intent = Intent(activity, TrainShow::class.java)
            val bundle = Bundle()
            bundle.putInt("id", trainingsSet.value!![position].id)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        })
        when (trainingsSet.value!![position].Type) {
            "Walking" -> holder.type.setImageResource(R.drawable.walk)
            "Running" -> holder.type.setImageResource(R.drawable.runner)
            "Cycling" -> holder.type.setImageResource(R.drawable.cycling)
        }

        holder.time.text = SimpleDateFormat("HH:mm").format(trainingsSet.value!![position].StartTime) +
                " -- " + SimpleDateFormat("HH:mm").format(trainingsSet.value!![position].EndTime)
        holder.title.text = trainingsSet.value!![position].Title
        holder.speed.text = trainingsSet.value!![position].Speed.toString() + " km/h"
        holder.distance.text = trainingsSet.value!![position].Distance.toString() + " km"
        holder.calories.text = trainingsSet.value!![position].CaloriesBurn.toString() + " kcal"

    }

    override fun getItemCount()= trainingsSet.value?.size ?:0
}