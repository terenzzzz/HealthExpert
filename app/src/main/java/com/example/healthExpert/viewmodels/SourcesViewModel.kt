package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Calories
import com.example.healthExpert.model.News
import com.example.healthExpert.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SourcesViewModel(private val fragment: Fragment) : ViewModel()  {
    var requestStatus = MutableLiveData<Int>()

    private val repository = NewsRepository()
    var news = MutableLiveData<MutableList<News>?>()
    var newInfo = MutableLiveData<MutableList<News>?>()

    fun getNews(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val newsParse = repository.getNews()

            // Refresh UI Update data
            if (newsParse != null) {
                if (newsParse.status != 200){
                    requestStatus.postValue(newsParse.status)
                }
                news.postValue(newsParse.data as MutableList<News>?)
            }

        }
    }

    fun getNewInfo(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = repository.getNewInfo(id)

            // Refresh UI Update data
            newInfo.postValue(updatedData)
        }
    }

//    private val sharedPreferences: SharedPreferences =
//        fragment.requireActivity().getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
//    private val token = sharedPreferences.getString("token","")

}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class SourcesViewModelFactory(private val activity: Fragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SourcesViewModel::class.java)) {
            return SourcesViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}