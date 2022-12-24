package com.example.healthExpert.view.sleep

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivitySleepBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.ArrayList

class Sleep : AppCompatActivity() {
    private lateinit var binding: ActivitySleepBinding

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

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        sleepSetUp(binding.sleepChart)
    }

    private fun sleepSetUp(view: View){
        // Find View
        val lineChart = view.findViewById<LineChart>(R.id.sleepChart)
        // Init data
        val list: MutableList<Entry> = ArrayList()
        list.add(Entry(0f,0f))
        list.add(Entry(1f,1f))
        list.add(Entry(2f,1f))
        list.add(Entry(3f,2f))
        list.add(Entry(4f,0f))
        list.add(Entry(5f,0f))
        list.add(Entry(6f,0f))

        // Create a list of x-axis labels
        val xAxisLabels = listOf("22", "00", "02", "04", "06", "08", "10")

        // Create a list of x-axis labels
        val yAxisLabels = listOf("Awake", "Light", "Deep")

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
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)        //X轴所在位置   默认为上面
        xAxis.setAxisMaximum(lineChart.xChartMax+1f);
        xAxis.setLabelCount(xAxisLabels.size)
        lineChart.axisLeft.valueFormatter = IndexAxisValueFormatter(yAxisLabels)
        lineChart.getAxisLeft().setAxisMaximum(lineChart.getYMax()+1f);
        lineChart.getAxisRight().setEnabled(false)
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER)
        lineDataSet.setColor(Color.rgb(52,179,241)) //折线的颜色
        lineDataSet.setLineWidth(4F)     //折线的粗细
        lineDataSet.setCircleColor(Color.BLACK)
        lineDataSet.setDrawValues(false)  //禁止显示点上的数值
        lineDataSet.setDrawCircles(false)
        lineChart.animateXY(1000, 1000);
        lineChart.invalidate() // 刷新
    }
}