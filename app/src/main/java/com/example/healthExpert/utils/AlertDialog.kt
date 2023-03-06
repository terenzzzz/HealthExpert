package com.example.healthExpert.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

class AlertDialog {
    fun showDialog(context:Context,title:String,message:String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which ->
            // Do something when the user clicks the "OK" button
        }

        val dialog = builder.create()
        dialog.show()
    }
}