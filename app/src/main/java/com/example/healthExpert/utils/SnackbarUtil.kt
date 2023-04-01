package com.example.healthExpert.utils

import android.os.Message
import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarUtil {

    companion object{
        fun buildNetwork(view:View){
            Snackbar.make(view, "Please Check Your Network", Snackbar.LENGTH_LONG).show()
        }

        fun buildTesting(view:View,code:Int){
            Snackbar.make(view, "Testing Status: $code", Snackbar.LENGTH_LONG).show()
        }
    }

}