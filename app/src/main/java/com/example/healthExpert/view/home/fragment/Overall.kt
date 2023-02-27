package com.example.login.view.homePage.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.OverallCompatFragment
import com.example.healthExpert.databinding.FragmentHistoryBinding
import com.example.healthExpert.databinding.FragmentOverallBinding
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.view.calories.Calories
import com.example.healthExpert.view.heart.Heart
import com.example.healthExpert.view.medication.Medication
import com.example.healthExpert.view.period.Period
import com.example.healthExpert.view.sleep.Sleep
import com.example.healthExpert.view.training.Train
import com.example.healthExpert.view.walk.Walk
import com.example.healthExpert.view.water.Water
import com.example.healthExpert.widget.Ring
import com.example.healthExpert.widget.RingView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.*


class Overall : OverallCompatFragment() {
    private lateinit var binding: FragmentOverallBinding
    private var todayDate = DateTimeConvert().toDate(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overallViewModel.caloriesAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                var intake = item.Intake
                var burn = item.Burn
                var total = burn?.let { intake?.minus(it) }
                var rate = total?.div(10f)
                binding.calories.setValueText(total.toString())
                binding.calories.setUnit("kcal")
                binding.caloriesValue.text = total.toString()
                if (rate != null) {
                    binding.calories.setSweepValue(rate.toFloat())
                }
            }
        })

        overallViewModel.walkAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                binding.walkProgress.progress = item.TotalSteps/100
                binding.walkRate.text = "${(item.TotalSteps/100)} %"
                binding.walkValue.text = item.TotalSteps.toString()
            }
        })

        overallViewModel.watersAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                binding.waterRing.setSweepValue((item.Total/80).toFloat())
                binding.waterRing.setValueText("${item.Total/80}%")
                binding.waterRing.setBgColor(Color.rgb(217, 217, 217))
                binding.waterRing.setSweepColor(Color.rgb(27, 204, 243))
                binding.waterValue.text = "${ item.Total.toFloat() / 1000 }"
            }
        })

        overallViewModel.trainingAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                binding.durationValue.text = "${ item.Duration.toFloat() / 60 } hours"
                binding.speedValue.text = "${ item.Speed } km/h"
                binding.distanceValue.text = "${ item.Distance } km"
                binding.trainCaloriesValue.text = "${ item.Calories } kcal"
            }
        })

        overallViewModel.medications.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null && list.size > 0) {
                // Update the UI
                binding.medicationName.text = list[0].Name
                binding.medicationDose.text = "${list[0].Dose} g"
                binding.medicationTime.text = DateTimeConvert().toHHmm(list[0].Date)
                when(list[0].Type){
                    "Capsule" -> binding.medicationIcon.setImageResource(R.drawable.capsule)
                    "Tablet" -> binding.medicationIcon.setImageResource(R.drawable.drug)
                    "Liquid" -> binding.medicationIcon.setImageResource(R.drawable.syrup)
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        overallViewModel.getCaloriesOverall(todayDate)
        overallViewModel.getWalksOverall(todayDate)
        overallViewModel.getWatersOverall(todayDate)
        overallViewModel.getTrainingOverall(todayDate)
        overallViewModel.medications(todayDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOverallBinding.inflate(layoutInflater)


        // Sleep set up
        sleepSetUp(binding.root)
        // Heart Set Up
        heartSetUp(binding.root)

        binding.caloriesBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Calories.startFn(it)
            }

        })

        binding.sleepBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Sleep.startFn(it)
            }
        })

        binding.stepBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Walk.startFn(it)
            }
        })

        binding.waterBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Water.startFn(it)
            }
        })

        binding.heartBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Heart.startFn(it)
            }
        })

        binding.trainBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Train.startFn(it)
            }
        })

        binding.medicalBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Medication.startFn(it)
            }
        })

        binding.periodBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Period.startFn(it)
            }
        })


        return  binding.root
    }



    private fun sleepSetUp(view: View){
        // Find View
        val lineChart = view.findViewById<LineChart>(R.id.sleepChart)
        // Init data
        val list: MutableList<Entry> = ArrayList()
        list.add(Entry(0f,2f))
        list.add(Entry(1f,4f))
        list.add(Entry(2f,3f))
        list.add(Entry(3f,5f))
        list.add(Entry(4f,1f))
        list.add(Entry(5f,6f))
        // Add data to Chart
        val lineDataSet = LineDataSet(list, "123")
        val lineData = LineData(lineDataSet)

        // Chart Setting
        lineChart.setData(lineData)
        lineChart.getXAxis().setDrawGridLines(false)  //是否绘制X轴上的网格线（背景里面的竖线）
        lineChart.getDescription().setEnabled(false)  //是否显示右下角描述
        lineChart.setTouchEnabled(false) // 禁止互动
        val legend: Legend = lineChart.getLegend()
        legend.isEnabled = false //是否显示图例
        val xAxis: XAxis = lineChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)        //X轴所在位置   默认为上面
        val AxisLeft: YAxis = lineChart.getAxisLeft()
        lineChart.getAxisRight().setEnabled(false)
        lineChart.getAxisLeft().setEnabled(false)
        lineChart.xAxis.setEnabled(false)
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER)
        lineDataSet.setColor(Color.BLACK) //折线的颜色
        lineDataSet.setLineWidth(2F)     //折线的粗细
        lineDataSet.setCircleRadius(0F)      //圆点的半径
        lineDataSet.setCircleColor(Color.BLACK)
        lineDataSet.setDrawCircleHole(false) //false表示直接画成实心圆
        lineDataSet.setDrawValues(false)  //禁止显示点上的数值
        lineDataSet.setDrawCircles(false)

        lineChart.animateXY(1000, 1000);
        lineChart.invalidate() // 刷新
    }

    private fun heartSetUp(view: View){
        // Find View
        val lineChart = view.findViewById<LineChart>(R.id.heartChart)
        // Init data
        val list: MutableList<Entry> = ArrayList()
        list.add(Entry(0f,3f))
        list.add(Entry(1f,5f))
        list.add(Entry(2f,2f))
        list.add(Entry(3f,4f))
        list.add(Entry(4f,5f))
        list.add(Entry(5f,3f))
        // Add data to Chart
        val lineDataSet = LineDataSet(list, "123")
        val lineData = LineData(lineDataSet)

        // Chart Setting
        lineChart.data = lineData
        lineChart.getXAxis().setDrawGridLines(false)  //是否绘制X轴上的网格线（背景里面的竖线）
        lineChart.getDescription().setEnabled(false)  //是否显示右下角描述
        lineChart.setTouchEnabled(false) // 禁止互动
        val legend: Legend = lineChart.getLegend()
        legend.isEnabled = false //是否显示图例
        val xAxis: XAxis = lineChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)        //X轴所在位置   默认为上面
        lineChart.getAxisRight().setEnabled(false)
        lineChart.getAxisLeft().setEnabled(false)
        lineChart.xAxis.setEnabled(false)
        lineDataSet.setColor(Color.rgb(255,82,82)) //折线的颜色
        lineDataSet.setLineWidth(4F)     //折线的粗细
        lineDataSet.setCircleRadius(0F)      //圆点的半径
        lineDataSet.setCircleColor(Color.BLACK)
        lineDataSet.setDrawCircleHole(false) //false表示直接画成实心圆
        lineDataSet.setDrawValues(false)  //禁止显示点上的数值
        lineDataSet.setDrawCircles(false)

        lineChart.animateXY(1000, 1000);
        lineChart.invalidate() // 刷新
    }
}