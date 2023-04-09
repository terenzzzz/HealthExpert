package com.example.healthExpert.view.home.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.OverallCompatFragment
import com.example.healthExpert.databinding.FragmentOverallBinding
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.calories.Calories
import com.example.healthExpert.view.heart.Heart
import com.example.healthExpert.view.medication.Medication
import com.example.healthExpert.view.period.Period
import com.example.healthExpert.view.sleep.Sleep
import com.example.healthExpert.view.training.Train
import com.example.healthExpert.view.walk.Walk
import com.example.healthExpert.view.water.Water
import com.example.healthExpert.widget.RoundedBarChart
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import java.util.*


class Overall : OverallCompatFragment() {
    private lateinit var binding: FragmentOverallBinding
    private var todayDate = DateTimeConvert.toDate(Date())
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences= requireActivity().getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)

        overallViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code != null){
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })

        overallViewModel.goals.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                val editor = sharedPreferences.edit()
                editor.putInt("caloriesGoal", item.Calories)
                editor.putInt("stepsGoal", item.Steps)
                editor.putInt("trainingGoal", item.Training)
                editor.putInt("sleepGoal", item.Sleep)
                editor.putInt("waterGoal", item.Water)
                editor.apply()
            }
        })

        overallViewModel.caloriesAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                val caloriesGoal = sharedPreferences.getInt("caloriesGoal",2400)
                var intake = item.Intake
                var burn = item.Burn
                var total = burn?.let { intake?.minus(it) }
                var rate = total?.div(caloriesGoal.toFloat())?.times(100)
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
                val stepsGoal = sharedPreferences.getInt("stepsGoal",10000)
                val progress = item.TotalSteps.toDouble() / stepsGoal.toDouble() * 100
                binding.walkProgress.progress = progress.toInt()
                binding.walkRate.text = "$progress %"
                binding.walkValue.text = item.TotalSteps.toString()
            }
        })

        overallViewModel.watersAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                val waterGoal = sharedPreferences.getInt("waterGoal",8000)
                val rate = item.Total.toDouble()/waterGoal.toDouble()*100
                binding.waterRing.setSweepValue(rate.toFloat())
                binding.waterRing.setValueText(String.format("%.0f", rate)+" %")
                binding.waterRing.setBgColor(Color.rgb(217, 217, 217))
                binding.waterRing.setSweepColor(Color.rgb(27, 204, 243))
                binding.waterValue.text = "${ item.Total.toFloat() / 1000 }"
            }
        })

        overallViewModel.trainingAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                binding.trainingValue.text = "${ item.Duration } "
                TrainingSetup(binding.trainingBar)
            }
        })

        overallViewModel.medications.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            Log.d("测试", "medications: $list")
            if (list != null) {
                // send time to service to pending notification
                for (item in list){
                    val intent = Intent("medication")
                    intent.putExtra("time", DateTimeConvert.toTime(item.Date))
                    activity?.sendBroadcast(intent)
                }
                // Update the UI
                if (list.size > 0){
                    binding.medicalNotice.visibility = View.VISIBLE
                    binding.medicationName.text = list[0].Name
                    binding.medicationDose.text = "${list[0].Dose} g"
                    binding.medicationTime.text = DateTimeConvert.toHHmm(list[0].Date)
                    when(list[0].Type){
                        "Capsule" -> binding.medicalType.setImageResource(R.drawable.capsule)
                        "Tablet" -> binding.medicalType.setImageResource(R.drawable.drug)
                        "Liquid" -> binding.medicalType.setImageResource(R.drawable.syrup)
                    }
                    binding.zeroNotice.visibility = View.GONE
                }
            }else{
                binding.medicalNotice.visibility = View.GONE
                binding.zeroNotice.visibility = View.VISIBLE
            }
        })

        overallViewModel.sleep.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null ) {
                binding.sleepValue.text = DateTimeConvert.toDecimalHours(
                    DateTimeConvert.toDateTime(item.StartTime),
                    DateTimeConvert.toDateTime(item.EndTime)
                )
            }
        })

    }

    override fun onResume() {
        super.onResume()
        overallViewModel.getGoals()
        todayDate = DateTimeConvert.toDate(Date())
        // Heart Set Up
        heartSetUp(binding.root)
        sleepSetUp(binding.root)
        overallViewModel.getCaloriesOverall(todayDate)
        overallViewModel.getWalksOverall(todayDate)
        overallViewModel.getWatersOverall(todayDate)
        overallViewModel.getTrainingOverall(todayDate)
        overallViewModel.medications(todayDate)
        overallViewModel.getSleep(todayDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOverallBinding.inflate(layoutInflater)


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

    private fun TrainingSetup(view: BarChart) {
        // Find View
        // Create data entries
        val entries = listOf(
            BarEntry(1f, 4f),
            BarEntry(2f, 6f),
            BarEntry(3f, 2f),
            BarEntry(4f, 8f),
            BarEntry(5f, 3f),
            BarEntry(6f, 5f),
            BarEntry(7f, 9f)
        )

        // Create a dataset with entries and label
        val barDataSet = BarDataSet(entries, "Sales")
        view.xAxis.setDrawGridLines(false)  //是否绘制X轴上的网格线（背景里面的竖线）
        view.axisLeft.setDrawGridLines(false)
        view.axisLeft.setEnabled(false)
        view.xAxis.setEnabled(false)
        view.axisRight.setDrawGridLines(false)
        view.axisRight.setEnabled(false)
        view.description.isEnabled = false  //是否显示右下角描述
        view.setTouchEnabled(false) // 禁止互动
        val legend: Legend = view.legend
        legend.isEnabled = false //是否显示图例
        barDataSet.setDrawValues(false)  //禁止显示点上的数值
        barDataSet.color = Color.rgb(44, 68, 99)
        view.renderer = RoundedBarChart(view, view.animator, view.viewPortHandler)


        // Create a BarData object with the dataset
        val data = BarData(barDataSet)

        // Set the data to the chart
        view.data = data


        // Set animation duration
        view.animateY(1000)

        // Invalidate the chart to refresh
        view.invalidate()
    }
}