package com.example.healthExpert.view.walk


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityWalkBinding
import com.example.healthExpert.widget.Ring
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class Walk : AppCompatActivity() {
    private lateinit var binding: ActivityWalkBinding

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
        setContentView(binding.root)

        //
        ringSetUp(binding.calories,84)
        walkChart(binding.walkChart)

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.settingBtn.setOnClickListener (View.OnClickListener { view ->
            WalkSetting.startFn(this)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })


    }
    private fun ringSetUp(view: View, value: Int){
        val ring = view.findViewById<Ring>(R.id.calories)
        var calories = value
        ring.setSweepValue(calories.toFloat())
        ring.setValueText("8,339")
        ring.setStateText("Active")
        ring.setUnit("Steps Goal: ")
        ring.setUnitValue("10,000")
        ring.setBgColor(Color.argb(20,0, 0, 0))
        ring.setSweepColor(Color.rgb(0, 0, 0))
    }

    private fun walkChart(view: View){
        // Find View
        val barChart = view.findViewById<BarChart>(R.id.walkChart)
        // Init data
        val entries: MutableList<BarEntry> = ArrayList()
        entries.add(BarEntry(0f, 100f))
        entries.add(BarEntry(1f, 50f))
        entries.add(BarEntry(2f, 300f))
        entries.add(BarEntry(3f, 120f))
        entries.add(BarEntry(4f, 100f))
        entries.add(BarEntry(5f, 1400f))
        entries.add(BarEntry(6f, 2000f))
        entries.add(BarEntry(7f, 700f))
        entries.add(BarEntry(8f, 3243f))
        entries.add(BarEntry(9f, 300f))
        entries.add(BarEntry(10f, 143f))
        entries.add(BarEntry(11f, 140f))


        // Set data
        val set = BarDataSet(entries, "Walking Steps")
        val data = BarData(set)


        // Create a list of x-axis labels
        val xAxisLabels = listOf("0", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22", "24")

        // Set the x-axis labels
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM        //X轴所在位置   默认为上面

        // Set the y-axis labels
        val yAxis = barChart.axisLeft


        // 设置
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data); // set the data and list of lables into chart
        barChart.description.isEnabled = false;
        barChart.setScaleEnabled(false) // disable zoom

        barChart.animateXY(1000, 1000);
        barChart.invalidate(); // refresh

    }


}