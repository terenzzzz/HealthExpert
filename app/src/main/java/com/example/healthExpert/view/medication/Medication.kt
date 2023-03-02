package com.example.healthExpert.view.medication

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
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
import com.example.healthExpert.view.login.Login
import java.util.*



class Medication : MedicationsCompatActivity() {
    private lateinit var binding: ActivityMedicationBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var todayDate = DateTimeConvert().toDate(Date())

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

        medicationsViewModel.medications.observe(this, Observer { list ->
            // Update the UI based on the value of MutableLiveData
            if (list != null) {
                // Update the UI
                recyclerView.adapter = MedicationsAdapter(medicationsViewModel.medications,this)
            }
        })

        recyclerView = findViewById (R.id.recycler_view)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        binding.settingBtn.setOnClickListener(View.OnClickListener { view ->
            MedicationSetting.startFn(this)
        })


        binding.backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.notificationBtn.setOnClickListener(View.OnClickListener { view ->
            // Create the NotificationChannel
            val name = "My Notification Channel"
            val descriptionText = "This is my notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("my_channel_01", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val builder = NotificationCompat.Builder(this, "my_channel_01")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("My Notification")
                .setContentText("This is my notification content")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val intent = Intent(this, Login::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                1,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )
            builder.setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        medicationsViewModel.medications(todayDate)
    }


}

// RecycleView Adapter
class MedicationsAdapter(private val medicationsSet: MutableLiveData<MutableList<com.example.healthExpert.model.Medication>?>,
                      private val activity:Context) : RecyclerView.Adapter<MedicationsAdapter.ViewHolder>(){

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
            }else{
                holder.name.paintFlags = holder.name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.dose.paintFlags = holder.dose.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        })
        holder.time.text = DateTimeConvert().toHHmm(medicationsSet.value!![position].Date)
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