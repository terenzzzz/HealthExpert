package com.example.healthExpert.view.water

import android.content.Context
import android.content.Intent
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
import com.example.healthExpert.compatActivity.WatersCompatActivity
import com.example.healthExpert.databinding.ActivityWaterBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class Water : WatersCompatActivity() {
    private lateinit var binding: ActivityWaterBinding
    private lateinit var recyclerView: RecyclerView

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Water::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaterBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.waterViewmodel = watersViewModel
        setContentView(binding.root)



        recyclerView = findViewById (R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)


        binding.add200.setOnClickListener (View.OnClickListener { view ->
            addShortcut(200)
        })

        binding.add330.setOnClickListener (View.OnClickListener { view ->
            addShortcut(330)
        })

        binding.add500.setOnClickListener (View.OnClickListener { view ->
            addShortcut(500)
        })

        binding.add1000.setOnClickListener (View.OnClickListener { view ->
            addShortcut(1000)
        })

        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.settingBtn.setOnClickListener (View.OnClickListener { view ->
            WaterSetting.startFn(this)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            WaterAdd.startFn(this)
        })
    }

    override fun onResume() {
        super.onResume()
        watersViewModel.watersAll.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                // Update the UI
                binding.rate.text = "${item.Total.div(80)} %"
            }
        })

        watersViewModel.waters.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                watersViewModel.updateWatersOverall()
                recyclerView.adapter = WatersAdapter(watersViewModel.waters,this)
                watersViewModel.getWatersOverall()
            }

        })
        watersViewModel.getWaters()
    }

    private fun addShortcut(value:Int){
        var type = ""
        when(value){
            200 -> type = "Coffee"
            330 -> type = "EnergyDrink"
            500 -> type = "Water"
            1000 -> type = "ColdDrink"
        }
        watersViewModel.addWaters(type,type,
            type,value, SimpleDateFormat("HH:mm").format(Date()))
        Snackbar.make(binding.root, "Record Added", Snackbar.LENGTH_LONG).show()
        watersViewModel.getWaters()
    }
}
// RecycleView Adapter
class WatersAdapter(private val waterSet: MutableLiveData<MutableList<com.example.healthExpert.model.Water>?>,
                      private val activity:Context) : RecyclerView.Adapter<WatersAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var title: TextView = itemView.findViewById(R.id.title)
        var content: TextView = itemView.findViewById(R.id.content)
        var value: TextView = itemView.findViewById(R.id.value)
        var time: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_waters_record,
            parent,false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(activity, WaterEdit::class.java)
            val bundle = Bundle()
            bundle.putInt("id", waterSet.value!![position].id)

            intent.putExtras(bundle)
            activity.startActivity(intent)
        })
        if (waterSet.value != null){
            if (waterSet.value!![position] != null){
                when(waterSet.value!![position].Type){
                    "Coffee" -> holder.icon.setImageResource(R.drawable.coffeecup)
                    "EnergyDrink" -> holder.icon.setImageResource(R.drawable.energydrink)
                    "Water"->holder.icon.setImageResource(R.drawable.glassofwater)
                    "ColdDrink"->holder.icon.setImageResource(R.drawable.colddrink)
                    "BubbleTea"->holder.icon.setImageResource(R.drawable.bubbletea)
                    "Beer"->holder.icon.setImageResource(R.drawable.beer)
                }
                holder.title.text = waterSet.value!![position].Title
                holder.content.text = waterSet.value!![position].Content
                holder.value.text = "+ ${waterSet.value!![position].Value} ml"
                holder.time.text = SimpleDateFormat("HH:mm").format(waterSet.value!![position].Time)
            }
        }

    }

    override fun getItemCount()= waterSet.value?.size ?:0
}

