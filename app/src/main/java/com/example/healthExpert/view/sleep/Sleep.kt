package com.example.healthExpert.view.sleep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.SleepCompatActivity
import com.example.healthExpert.databinding.ActivitySleepBinding
import com.example.healthExpert.utils.AlertDialog
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import java.util.*


class Sleep : SleepCompatActivity() {
    private lateinit var binding: ActivitySleepBinding
    private var todayDate = DateTimeConvert.toDate(Date())
    var mode = "edit"

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Sleep::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySleepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null && bundle.getString("selectedDate") != "") {
            todayDate = bundle.getString("selectedDate").toString()
            mode = "view"
            binding.addBtn.visibility = View.GONE
        }


        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            SleepRecord.startFn(this)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.humidityInfo.setOnClickListener (View.OnClickListener { view ->
            val title = "Humidity Info"
            val message = "Medical research shows that the best humidity range for the human body is 45%-60%."
            AlertDialog().showDialog(this,title,message)
        })

        binding.temperatureInfo.setOnClickListener (View.OnClickListener { view ->
            val title = "Temperature Info"
            val message = "According to relevant research: when the bedroom temperature is 20-25℃, " +
                    "the human body feels the most comfortable."
            AlertDialog().showDialog(this,title,message)
        })

        binding.pressureInfo.setOnClickListener (View.OnClickListener { view ->
            val title = "Pressure Info"
            val message = "People feel sleepy when air pressure is higher or lower than normal"
            AlertDialog().showDialog(this,title,message)
        })

        binding.lightInfo.setOnClickListener (View.OnClickListener { view ->
            val title = "Light Info"
            val message = "Light is a very strong signal. When it enters the eyes, it will interfere with the sleep mechanism in the brain, " +
                    "reduce the amount of melatonin secretion, and affect the depth and quality of sleep"
            AlertDialog().showDialog(this,title,message)
        })

//        sleepSetUp(binding.sleepChart)
        sleepViewModel.sleep.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null){
                binding.date.text = DateTimeConvert.toDate(item.StartTime)
                binding.startTime.text = DateTimeConvert.toHHmm(item.StartTime)
                binding.endTime.text = DateTimeConvert.toHHmm(item.EndTime)
                binding.startDate.text = DateTimeConvert.toDate(item.StartTime)
                binding.endDate.text = DateTimeConvert.toDate(item.EndTime)
                binding.humidityValue.text =  String.format("%.2f", item.Humidity)
                binding.temperatureValue.text = String.format("%.2f", item.Temperature)
                binding.pressureValue.text = String.format("%.2f", item.Pressure)
                binding.lightValue.text = String.format("%.2f", item.Light)
                binding.durationValue.text = DateTimeConvert.subTimes(DateTimeConvert.toDateTime(item.StartTime),
                    DateTimeConvert.toDateTime(item.EndTime))
            }else{
                SnackbarUtil.buildNetwork(binding.root)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        sleepViewModel.getSleep(todayDate)
    }


//    private fun sleepSetUp(view: View){
//        // Find View
//        val lineChart = view.findViewById<LineChart>(R.id.sleepChart)
//        // Init data
//        val list: MutableList<Entry> = ArrayList()
//        list.add(Entry(0f,0f))
//        list.add(Entry(1f,1f))
//        list.add(Entry(2f,1f))
//        list.add(Entry(3f,2f))
//        list.add(Entry(4f,0f))
//        list.add(Entry(5f,0f))
//        list.add(Entry(6f,0f))
//
//        // Create a list of x-axis labels
//        val xAxisLabels = listOf("22", "00", "02", "04", "06", "08", "10")
//
//        // Create a list of x-axis labels
//        val yAxisLabels = listOf("Awake", "Light", "Deep")
//
//        // Add data to Chart
//        val lineDataSet = LineDataSet(list, "123")
//        val lineData = LineData(lineDataSet)
//
//        // Chart Setting
//        lineChart.setData(lineData)
//        lineChart.getXAxis().setDrawGridLines(false)  //是否绘制X轴上的网格线（背景里面的竖线）
//        lineChart.getDescription().setEnabled(false)  //是否显示右下角描述
//        lineChart.setTouchEnabled(false) // 禁止互动
//        val legend: Legend = lineChart.getLegend()
//        legend.isEnabled = false //是否显示图例
//        val xAxis: XAxis = lineChart.getXAxis()
//        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)        //X轴所在位置   默认为上面
//        xAxis.setAxisMaximum(lineChart.xChartMax+1f);
//        xAxis.setLabelCount(xAxisLabels.size)
//        lineChart.axisLeft.valueFormatter = IndexAxisValueFormatter(yAxisLabels)
//        lineChart.getAxisLeft().setAxisMaximum(lineChart.getYMax()+1f);
//        lineChart.getAxisRight().setEnabled(false)
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER)
//        lineDataSet.setColor(Color.rgb(52,179,241)) //折线的颜色
//        lineDataSet.setLineWidth(4F)     //折线的粗细
//        lineDataSet.setCircleColor(Color.BLACK)
//        lineDataSet.setDrawValues(false)  //禁止显示点上的数值
//        lineDataSet.setDrawCircles(false)
//        lineChart.animateXY(1000, 1000);
//        lineChart.invalidate() // 刷新
//    }
}