package com.example.healthExpert.viewmodels


import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.healthExpert.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val activity: AppCompatActivity) : ViewModel() {
    var loginRepo = LoginRepository(activity)
    // Log in
    var loginStatus: LiveData<Int> = MutableLiveData()
    var idUser: LiveData<Int> = MutableLiveData()


    fun login(email:String, password:String){
        viewModelScope.launch {
            loginRepo.login(email,password)
        }
    }

}

//class LoginViewModelFactory(private val userRepo: LoginRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
//            return LoginViewModel(userRepo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}