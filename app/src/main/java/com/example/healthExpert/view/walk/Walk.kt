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
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.widget.Ring
import com.example.healthExpert.widget.RoundedBarChart
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList


class Walk : WalkCompatActivity() {
    private lateinit var binding: ActivityWalkBinding
    private lateinit var ring: Ring
    private lateinit var barChart: BarChart
    private var todayDate = DateTimeConvert.toDate(Date())


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

        val bundle = intent.extras
        if (bundle != null && bundle.getString("selectedDate") != "") {
            todayDate = bundle.getString("selectedDate").toString()
            binding.settingBtn.visibility = View.GONE
        }


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
        walkViewModel.walkAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                ring.setValueText(item.TotalSteps.toString())
                ring.setSweepValue(item.TotalSteps.div(100).toFloat())
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })

        walkViewModel.walkSteps.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null && list.size>0){
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
        walkViewModel.updateWalksOverall()
        walkViewModel.getWalksOverall(todayDate)
        walkViewModel.getWalkSteps(todayDate)
    }


    private fun ringSetUp(view: View): Ring {
        val ring = view.findViewById<Ring>(R.id.calories)
        ring.setSweepValue(0f)
        ring.setValueText("0")
        ring.setStateText("Active")
        ring.setUnit("Steps Goal: ")
        ring.setUnitValue("10,000")
        ring.setBgColor(Color.argb(20,0, 0, 0))
        ring.setSweepColor(Color.rgb(0, 0, 0))
        return ring
    }


    private fun walkChart(view: View): BarChart {
        // Find View
        val barChart = view.findViewById<BarChart>(R.id.walkChart)
        // Init data
        val entries: MutableList<BarEntry> = ArrayList()
        for (i in 0..23) {
            entries.add(BarEntry(i.toFloat(), 0f))
        }
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
        barChart.renderer = RoundedBarChart(barChart, barChart.animator, barChart.viewPortHandler)

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