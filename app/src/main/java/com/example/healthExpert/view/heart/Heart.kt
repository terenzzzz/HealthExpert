package com.example.healthExpert.view.heart

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityHeartBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.ArrayList

class Heart : AppCompatActivity() {
    private lateinit var binding: ActivityHeartBinding

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Heart::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        heartSetUp(binding.heartChart)

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            HeartRecord.startFn(this)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })


    }

    private fun heartSetUp(view: View){
        // Find View
        val lineChart = view.findViewById<LineChart>(R.id.heartChart)
        // Init data
        val list: MutableList<Entry> = ArrayList()
        list.add(Entry(0f,60f))
        list.add(Entry(1f,78f))
        list.add(Entry(2f,80f))
        list.add(Entry(3f,72f))
        list.add(Entry(4f,94f))
        list.add(Entry(5f,85f))
        list.add(Entry(6f,62f))

        // Create a list of x-axis labels
        val xAxisLabels = listOf("00", "04", "08", "12", "16", "20", "24")

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
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        xAxis.setAxisMaximum(lineChart.xChartMax+1f);
        lineChart.getAxisRight().setEnabled(false)
        lineDataSet.setColor(Color.rgb(255,82,82)) //折线的颜色
        lineDataSet.setLineWidth(4F)     //折线的粗细
        lineDataSet.setCircleRadius(0F)      //圆点的半径
        lineDataSet.setCircleColor(Color.BLACK)
        lineDataSet.setDrawCircleHole(false) //false表示直接画成实心圆

        lineChart.animateXY(1000, 1000);
        lineChart.invalidate() // 刷新
    }
}