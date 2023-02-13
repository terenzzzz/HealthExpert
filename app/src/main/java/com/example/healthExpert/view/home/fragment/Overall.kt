package com.example.login.view.homePage.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.healthExpert.R
import com.example.healthExpert.view.calories.Calories
import com.example.healthExpert.view.heart.Heart
import com.example.healthExpert.view.medication.Medication
import com.example.healthExpert.view.period.Period
import com.example.healthExpert.view.sleep.Sleep
import com.example.healthExpert.view.training.Train
import com.example.healthExpert.view.walk.Walk
import com.example.healthExpert.view.water.Water
import com.example.healthExpert.widget.Ring
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.ArrayList


class Overall : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_overall, container, false)



        // Ring set up
        ringSetUp(mView)
        // Sleep set up
        sleepSetUp(mView)
        // Heart Set Up
        heartSetUp(mView)
        // Water ring Set Up
        waterRingSetUp(mView)

        // Block Listener
        val caloriesBlock = mView.findViewById<CardView>(R.id.caloriesBlock)
        val sleepBlock = mView.findViewById<CardView>(R.id.sleepBlock)
        val stepBlock = mView.findViewById<CardView>(R.id.stepBlock)
        val waterBlock = mView.findViewById<CardView>(R.id.waterBlock)
        val heartBlock = mView.findViewById<CardView>(R.id.heartBlock)
        val trainBlock = mView.findViewById<CardView>(R.id.trainBlock)
        val medicalBlock = mView.findViewById<CardView>(R.id.medicalBlock)
        val periodBlock = mView.findViewById<CardView>(R.id.periodBlock)


        caloriesBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Calories.startFn(it)
            }

        })

        sleepBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Sleep.startFn(it)
            }
        })

        stepBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Walk.startFn(it)
            }
        })

        waterBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Water.startFn(it)
            }
        })

        heartBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Heart.startFn(it)
            }
        })

        trainBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Train.startFn(it)
            }
        })

        medicalBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Medication.startFn(it)
            }
        })

        periodBlock.setOnClickListener(View.OnClickListener { view ->
            this.context?.let {
                Period.startFn(it)
            }
        })


        return  mView
    }

    private fun ringSetUp(view: View){
        val ring = view.findViewById<Ring>(R.id.calories)
        var calories = 65
        ring.setSweepValue(calories.toFloat())
        ring.setValueText("962")
        ring.setUnit("kcal")
    }

    private fun waterRingSetUp(view: View){
        val ring = view.findViewById<Ring>(R.id.water_ring)
        var water = 65
        ring.setSweepValue(water.toFloat())
        ring.setValueText("50%")
        ring.setBgColor(Color.rgb(217, 217, 217))
        ring.setSweepColor(Color.rgb(27, 204, 243))
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