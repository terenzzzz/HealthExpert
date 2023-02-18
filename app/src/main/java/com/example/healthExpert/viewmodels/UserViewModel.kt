package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.healthExpert.model.User
import com.example.healthExpert.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.String.format
import java.util.*
import kotlin.math.pow

class UserViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    private val repository = UserRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var user = MutableLiveData<User?>(null)

    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.getUserInfo(it) }

            // Refresh UI Update data
            user.postValue(updatedData)

        }
    }

    fun editName(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                repository.editName(token,name)
            }
        }
    }

    fun editGender(gender:String){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val bmi = calcBMI(user.value!!.Weight,user.value!!.Height)
                val bfr = calcBFR(bmi, user.value!!.Age,gender)
                repository.editGender(token,gender)
                repository.editBodyFatRate(token,bfr)
            }
        }
    }

    fun editAge(age:Int){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val bmi = calcBMI(user.value!!.Weight,user.value!!.Height)
                val bfr = calcBFR(bmi, age, user.value!!.Gender)
                repository.editAge(token,age)
                repository.editBodyFatRate(token,bfr)
            }
        }
    }

    fun editWeight(weight:Float){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val bmi = calcBMI(weight,user.value!!.Height)
                val bfr = calcBFR(bmi, user.value!!.Age,user.value!!.Gender)
                repository.editWeight(token,weight)
                repository.editBmi(token,bmi)
                repository.editBodyFatRate(token,bfr)
            }
        }
    }

    fun editHeight(height:Float){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val bmi = calcBMI(user.value!!.Weight,height)
                val bfr = calcBFR(bmi, user.value!!.Age,user.value!!.Gender)
                repository.editHeight(token,height)
                repository.editBmi(token,bmi)
                repository.editBodyFatRate(token,bfr)
            }
        }
    }

    fun calcBMI(weight: Float,height: Float) : Float{
        //        BMI=体重（公斤）÷（身高×身高）（米）。
        return String.format("%.1f",weight.div(height.div(100).pow(2))).toFloat()
    }

    fun calcBFR(bmi: Float,age:Int,gender: String): Float{
        //        体脂率：1.2 x BMI + 0.23 x 年齡 – 5.4 -10.8 x 性別（男生的值為 1，女生為 0）
        var genderInt = if (gender == "Male"){
            1
        }else{
            0
        }
        val bfr = 1.2.times(bmi)+0.23.times(age)-5.4-10.8.times(genderInt)
        return String.format("%.1f",bfr).toFloat()
    }
}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class UserViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}