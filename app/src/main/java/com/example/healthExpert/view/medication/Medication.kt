package com.example.healthExpert.view.medication

import android.app.*
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.MedicationsCompatActivity
import com.example.healthExpert.databinding.ActivityMedicationBinding
import com.example.healthExpert.utils.DateTimeConvert
import com.example.healthExpert.utils.SnackbarUtil
import com.example.healthExpert.view.home.Home
import com.example.healthExpert.view.login.Login
import com.example.healthExpert.viewmodels.MedicationsViewModel
import java.util.*


class Medication : MedicationsCompatActivity() {
    private lateinit var binding: ActivityMedicationBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var todayDate = DateTimeConvert.toDate(Date())

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, Medication::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dateTime.text = todayDate

        medicationsViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code!=null){
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })

        medicationsViewModel.medications.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                recyclerView.adapter = MedicationsAdapter(medicationsViewModel.medications,medicationsViewModel)
            }
        })

        recyclerView = findViewById (R.id.recycler_view)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager


        binding.backBtn.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.addBtn.setOnClickListener {
            MedicationAdd.startFn(this)
        }

        binding.settingBtn.setOnClickListener{
            MedicationSetting.startFn(this)
        }
    }

    override fun onResume() {
        super.onResume()
        medicationsViewModel.medications(todayDate)
    }


}

// RecycleView Adapter
class MedicationsAdapter(private val medicationsSet: MutableLiveData<MutableList<com.example.healthExpert.model.Medication>?>,
                      private val medicationsViewModel:MedicationsViewModel) : RecyclerView.Adapter<MedicationsAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var time: TextView = itemView.findViewById(R.id.time)
        var type: ImageView = itemView.findViewById(R.id.type)
        var name: TextView = itemView.findViewById(R.id.name)
        var dose: TextView = itemView.findViewById(R.id.dose)
        var radioBtn: RadioButton = itemView.findViewById(R.id.radio_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_medicine_record,
            parent,false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener { view ->
            holder.radioBtn.isChecked = !holder.radioBtn.isChecked
            if (holder.radioBtn.isChecked){
                holder.name.paintFlags = holder.name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.dose.paintFlags = holder.name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                if (medicationsSet.value!![position].id!=null){
                    medicationsViewModel.editMedicationStatus((medicationsSet.value!![position].id).toString(),"1")
                }
            }else{
                holder.name.paintFlags = holder.name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.dose.paintFlags = holder.dose.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                if (medicationsSet.value!![position].id!=null){
                    medicationsViewModel.editMedicationStatus((medicationsSet.value!![position].id).toString(),"0")
                }

            }
        })
        holder.time.text = DateTimeConvert.toHHmm(medicationsSet.value!![position].Date)
        holder.name.text = medicationsSet.value!![position].Name
        holder.dose.text = medicationsSet.value!![position].Dose.toString()

        when(medicationsSet.value!![position].Type){
            "Capsule" -> holder.type.setImageResource(R.drawable.capsule)
            "Tablet" -> holder.type.setImageResource(R.drawable.drug)
            "Liquid" -> holder.type.setImageResource(R.drawable.syrup)
        }

        if(medicationsSet.value!![position].Status == 1){
            holder.radioBtn.isChecked = true
            holder.name.paintFlags = holder.name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.dose.paintFlags = holder.name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount()= medicationsSet.value?.size ?:0
}