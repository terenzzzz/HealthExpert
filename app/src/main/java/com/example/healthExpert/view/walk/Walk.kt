package com.example.healthExpert.view.walk


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.WalkCompatActivity
import com.example.healthExpert.databinding.ActivityWalkBinding
import com.example.healthExpert.widget.Ring
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList


class Walk : WalkCompatActivity() {
    private lateinit var binding: ActivityWalkBinding
    private lateinit var ring: Ring
    private lateinit var barChart: BarChart


    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Walk::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.walkViewmodel = walkViewModel
        setContentView(binding.root)




        ring = ringSetUp(binding.calories)
        barChart = walkChart(binding.walkChart)


        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.settingBtn.setOnClickListener (View.OnClickListener { view ->
            WalkSetting.startFn(this)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

    }

    override fun onResume() {
        super.onResume()


        walkViewModel.walk.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                ring.setValueText(item.TotalSteps.toString())
                ring.setSweepValue(item.TotalSteps.div(100).toFloat())
            }
        })

        walkViewModel.walkSteps.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null){
                val entries: MutableList<BarEntry> = ArrayList()
                val map = mutableMapOf<String, Int>()
                for (ws in list){
                    val hour = SimpleDateFormat("HH").format(ws.Time)
                    if (map.containsKey(hour)){
                        map[hour] = map[hour]!!.plus(ws.Steps)
                    }else{
                        map[hour] = ws.Steps
                    }

                }
                Log.d("Walk", map.toString())

                for (i in 0..23) {
                    for ((key, value) in map) {
                        if (i == key.toInt()){
                            entries.add(BarEntry(i.toFloat(), value.toFloat()))
                        }else{
                            entries.add(BarEntry(i.toFloat(), 0f))
                        }
                    }
                }
                barChart.data = setBarchartData(entries)
            }
        })
        walkViewModel.getWalks()
        walkViewModel.getWalkSteps()
    }


    private fun ringSetUp(view: View): Ring {
        val ring = view.findViewById<Ring>(R.id.calories)
        ring.setSweepValue(84f)
        ring.setValueText("0")
        ring.setStateText("Active")
        ring.setUnit("Steps Goal: ")
        ring.setUnitValue("10,000")
        ring.setBgColor(Color.argb(20,0, 0, 0))
        ring.setSweepColor(Color.rgb(0, 0, 0))
        return ring
    }

    private fun getTodayDate(): String? {
        val date = Date(System.currentTimeMillis())
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(date)
        Log.d("walk", "今日时间: $sdf")
    }

    private fun walkChart(view: View): BarChart {
        // Find View
        val barChart = view.findViewById<BarChart>(R.id.walkChart)
        // Init data
        val entries: MutableList<BarEntry> = ArrayList()
        // Set data
        val data = setBarchartData(entries)
        // Set the x-axis labels
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM        //X轴所在位置   默认为上面
        // Set the y-axis labels
        val yAxis = barChart.axisLeft
        // 设置

        barChart.data = data; // set the data and list of lables into chart
        barChart.description.isEnabled = false;
        // disable pinch zoom on the chart

        barChart.setScaleEnabled(false) // D zoom

        barChart.animateXY(1000, 1000);
        barChart.invalidate(); // refresh
        return barChart
    }

    private fun setBarchartData(entries: MutableList<BarEntry>): BarData {
        // Set data
        val set = BarDataSet(entries, "Walking Steps")
        return BarData(set)
    }



}