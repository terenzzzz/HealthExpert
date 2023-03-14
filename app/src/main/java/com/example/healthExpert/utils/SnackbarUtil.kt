package com.example.healthExpert.utils

import android.os.Message
import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarUtil {

    fun buildNetwork(view:View){
        Snackbar.make(view, "Please Check Your Network", Snackbar.LENGTH_LONG).show()
    }
}