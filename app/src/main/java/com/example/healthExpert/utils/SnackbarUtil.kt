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
            Snackbar.make(view, "Operation Failed! Please Check Your Network! ", Snackbar.LENGTH_LONG).show()
        }

        fun buildPassword(view:View,code:Int){
            Snackbar.make(view, "Please Ensure Your Password is Correct(8-16 characters,1 uppercase,1 lowercase and 1 number)", Snackbar.LENGTH_LONG).show()
        }
    }

}