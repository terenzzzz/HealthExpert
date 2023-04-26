package com.example.healthExpert.view.heart

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
import com.example.healthExpert.compatActivity.HeartRateCompatActivity
import com.example.healthExpert.databinding.ActivityHeartBinding
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.HeartRate
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.view.calories.CaloriesAdapter
import com.example.healthExpert.view.calories.CaloriesEdit
import com.example.healthExpert.widget.Ring
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.SimpleFormatter
import kotlin.collections.ArrayList
import kotlin.math.log


class Heart : HeartRateCompatActivity() {
    private lateinit var binding: ActivityHeartBinding
    private lateinit var lineChart: LineChart
    private var todayDate = DateTimeConvert.toDate(Date())
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

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
        binding.dateTime.text = todayDate

        lineChart = heartSetUp(binding.heartChart)
        recyclerView = findViewById (R.id.recycler_view)
        layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager


        heartRateViewModel.heartRates.observe(this, Observer { heartRates ->
            // Update the UI based on the value of MutableLiveData
            if (heartRates != null &&  heartRates.isNotEmpty()) {
                recyclerView.adapter = HeartRateAdapter(heartRateViewModel.heartRates,this)

                binding.avgBpm.text = String.format("%.0f", heartRates.map { it.HeartRate.toInt() }.average()) + " BPM"
                binding.maxBpm.text = "${heartRates.maxBy { it.HeartRate }.HeartRate} BPM"

                // Chart
                val list: MutableList<Entry> = ArrayList()
                for (i in 0 until 24) {
                    list.add(Entry(i.toFloat(),0f))
                }
                for (hr in heartRates){
                    val hour = SimpleDateFormat("HH").format(hr.Date)
                    if (list[hour.toInt()].y != 0f){
                        list[hour.toInt()].y = (list[hour.toInt()].y + hr.HeartRate.toFloat())/2
                    }else{
                        list[hour.toInt()].y = hr.HeartRate.toFloat()
                    }
                }

                val lineDataSet = LineDataSet(list, "123")
                val lineData = LineData(lineDataSet)

                lineDataSet.setDrawValues(true)

                lineChart.data = lineData
                lineDataSet.setColor(Color.rgb(255,82,82)) //折线的颜色
                lineDataSet.setLineWidth(4F)     //折线的粗细
                lineDataSet.setCircleRadius(0F)      //圆点的半径
                lineDataSet.setCircleColor(Color.rgb(255,82,82))
                lineDataSet.setDrawCircleHole(false) //false表示直接画成实心圆

            }
        })

        binding.addBtn.setOnClickListener {
            HeartRecord.startFn(this)
        }

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })


    }

    override fun onResume() {
        super.onResume()
        heartRateViewModel.getHeartRates(todayDate)

    }

    private fun heartSetUp(view: View): LineChart {
        // Find View
        val lineChart = view.findViewById<LineChart>(R.id.heartChart)
        // Init data
        val list: MutableList<Entry> = ArrayList()
        for (i in 0 until 24) {
            list.add(Entry(i.toFloat(),0f))
        }

        // Add data to Chart
        val lineDataSet = LineDataSet(list, "123")
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        // Chart Setting
//        lineChart.getXAxis().setDrawGridLines(false)  //是否绘制X轴上的网格线（背景里面的竖线）
        lineChart.getDescription().setEnabled(false)  //是否显示右下角描述
        lineChart.setTouchEnabled(false) // 禁止互动
        lineChart.getAxisRight().setEnabled(false)

        val legend: Legend = lineChart.getLegend()
        legend.isEnabled = false //是否显示图例
        val xAxis: XAxis = lineChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)        //X轴所在位置   默认为上面



        lineDataSet.setColor(Color.rgb(255,82,82)) //折线的颜色
        lineDataSet.setLineWidth(4F)     //折线的粗细
        lineDataSet.setCircleRadius(0F)      //圆点的半径
        lineDataSet.setCircleColor(Color.rgb(255,82,82))
        lineDataSet.setDrawCircleHole(false) //false表示直接画成实心圆

        lineChart.animateXY(1000, 1000);
        lineChart.invalidate() // 刷新

        return lineChart
    }
}

// RecycleView Adapter
class HeartRateAdapter(private val heartRateSet: MutableLiveData<MutableList<HeartRate>?>,
                      private val activity:Context) : RecyclerView.Adapter<HeartRateAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var bpm: TextView = itemView.findViewById(R.id.bpm)
        var time: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_heartrate_record,
            parent,false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bpm.text = "${heartRateSet.value!![position].HeartRate} BPM"
        holder.time.text = DateTimeConvert.toHHmm(heartRateSet.value!![position].Date)
    }

    override fun getItemCount()= heartRateSet.value?.size ?:0
}