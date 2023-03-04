package com.example.healthExpert.view.medication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.MedicationsCompatActivity
import com.example.healthExpert.databinding.ActivityMedicationSettingBinding
import com.example.healthExpert.model.Medication
import com.example.healthExpert.utils.DateTimeConvert

class MedicationSetting : MedicationsCompatActivity() {
    private lateinit var binding: ActivityMedicationSettingBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, MedicationSetting::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicationsViewModel.pendingMedications.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                recyclerView.adapter = MedicationSettingAdapter(medicationsViewModel.pendingMedications,this)
            }
        })

        recyclerView = findViewById (R.id.recycler_view)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager



        binding.backBtn.setOnClickListener (View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.addBtn.setOnClickListener (View.OnClickListener { view ->
            MedicationAdd.startFn(this)
        })
    }

    override fun onResume() {
        super.onResume()
        medicationsViewModel.pendingMedications()
    }
}

// RecycleView Adapter
class MedicationSettingAdapter(private val medicationsSet: MutableLiveData<MutableList<Medication>?>,
                         private val activity:Context) : RecyclerView.Adapter<MedicationSettingAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var date: TextView = itemView.findViewById(R.id.date)
        var time: TextView = itemView.findViewById(R.id.time)
        var type: ImageView = itemView.findViewById(R.id.type)
        var name: TextView = itemView.findViewById(R.id.name)
        var dose: TextView = itemView.findViewById(R.id.dose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_medicine_setting_record,
            parent,false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener { view ->
            Log.d("adapter", medicationsSet.value!![position].id.toString())

            val intent = Intent(activity, MedicationEdit::class.java)
            val bundle = Bundle()
            bundle.putString("id", medicationsSet.value!![position].id.toString())
            intent.putExtras(bundle)
            activity.startActivity(intent)

        })
        holder.date.text = DateTimeConvert().toDate(medicationsSet.value!![position].Date)
        holder.time.text = DateTimeConvert().toHHmm(medicationsSet.value!![position].Date)
        holder.name.text = medicationsSet.value!![position].Name
        holder.dose.text = medicationsSet.value!![position].Dose.toString()

        when(medicationsSet.value!![position].Type){
            "Capsule" -> holder.type.setImageResource(R.drawable.capsule)
            "Tablet" -> holder.type.setImageResource(R.drawable.drug)
            "Liquid" -> holder.type.setImageResource(R.drawable.syrup)
        }

    }

    override fun getItemCount()= medicationsSet.value?.size ?:0
}