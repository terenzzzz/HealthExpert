package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.healthExpert.model.User
import com.example.healthExpert.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.pow

class UserViewModel(private val activity: AppCompatActivity) : ViewModel()  {
    var requestStatus = MutableLiveData<Int>()
    var passwordStatus = MutableLiveData<Int>()

    private val repository = UserRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var user = MutableLiveData<User?>(null)

    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val userInfoParse = token?.let { repository.getUserInfo(it) }

            if (userInfoParse != null) {
                if (userInfoParse.status != 200){
                    requestStatus.postValue(userInfoParse.status)
                }else{
                    user.postValue(userInfoParse.data)
                }
            }
        }
    }

    fun editName(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val resStatus = repository.editName(token,name)
                if (resStatus != 200){
                    requestStatus.postValue(resStatus)
                }
            }
        }
    }

    fun editGender(gender:String){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                if (user.value != null){
                    val bmi = calcBMI(user.value!!.Weight, user.value!!.Height)
                    val bfr = calcBFR(bmi, user.value!!.Age,gender)
                    val resStatus1 = repository.editGender(token,gender)
                    val resStatus2 = repository.editBodyFatRate(token,bfr)
                    if (resStatus1 != 200){
                        requestStatus.postValue(resStatus1)
                    }
                    if (resStatus2 != 200){
                        requestStatus.postValue(resStatus2)
                    }
                }
            }
        }
    }

    fun editAge(age:Int){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val bmi = calcBMI(user.value!!.Weight,user.value!!.Height)
                val bfr = calcBFR(bmi, age, user.value!!.Gender)
                val resStatus1 = repository.editAge(token,age)
                val resStatus2 = repository.editBodyFatRate(token,bfr)
                if (resStatus1 != 200){
                    requestStatus.postValue(resStatus1)
                }
                if (resStatus2 != 200){
                    requestStatus.postValue(resStatus2)
                }
            }
        }
    }

    fun editWeight(weight:Float){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val bmi = calcBMI(weight,user.value!!.Height)
                val bfr = calcBFR(bmi, user.value!!.Age,user.value!!.Gender)
                val resStatus1 = repository.editWeight(token,weight)
                val resStatus2 = repository.editBmi(token,bmi)
                repository.editBodyFatRate(token,bfr)
                if (resStatus1 != 200){
                    requestStatus.postValue(resStatus1)
                }
                if (resStatus2 != 200){
                    requestStatus.postValue(resStatus2)
                }
            }
        }
    }

    fun editHeight(height:Float){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val bmi = calcBMI(user.value!!.Weight,height)
                val bfr = calcBFR(bmi, user.value!!.Age,user.value!!.Gender)
                val resStatus1 = repository.editHeight(token,height)
                val resStatus2 = repository.editBmi(token,bmi)
                val resStatus3 = repository.editBodyFatRate(token,bfr)
                if (resStatus1 != 200){
                    requestStatus.postValue(resStatus1)
                }
                if (resStatus2 != 200){
                    requestStatus.postValue(resStatus2)
                }
                if (resStatus3 != 200){
                    requestStatus.postValue(resStatus3)
                }
            }
        }
    }

    fun changePassword(password:String,newPassword:String){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val resStatus = repository.changePassword(token,password, newPassword)
                passwordStatus.postValue(resStatus)
            }
        }
    }

    fun initUser(height:String,weight:String,age:String,gender:String,name:String){
        viewModelScope.launch(Dispatchers.IO) {
            if (token != null) {
                val resStatus = repository.initUser(token,height, weight,age,gender,name)
                if (resStatus != 200){
                    requestStatus.postValue(resStatus)
                }
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