package com.example.healthExpert.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthExpert.model.Medication
import com.example.healthExpert.repository.MedicationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationsViewModel(private val activity: AppCompatActivity) : ViewModel() {
    var requestStatus = MutableLiveData<Int?>()

    private val repository = MedicationRepository()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("healthy_expert", AppCompatActivity.MODE_PRIVATE)
    private val token = sharedPreferences.getString("token","")
    var medications = MutableLiveData<MutableList<Medication>?>()
    var medication = MutableLiveData<Medication?>()
    var pendingMedications = MutableLiveData<MutableList<Medication>?>()



    fun medications(date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val medicationsParse = token?.let { repository.medications(it,date) }
            Log.d("测试", "medication: ${medicationsParse!!.status}")
            // Refresh UI Update data
            if (medicationsParse != null) {
                if (medicationsParse.status != 200){
                    requestStatus.postValue(medicationsParse.status)
                }
                medications.postValue(medicationsParse.data as MutableList<Medication>?)
            }
        }
    }

    fun medication(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val medicationsParse = token?.let { repository.medication(token,id) }

            // Refresh UI Update data
            if (medicationsParse != null) {
                if (medicationsParse.status != 200){
                    requestStatus.postValue(medicationsParse.status)
                }
                medication.postValue(medicationsParse.data?.get(0))
            }
        }
    }

    fun pendingMedications(){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val medicationsParse = token?.let { repository.pendingMedications(token) }
            if (medicationsParse != null) {
                if (medicationsParse.status!=200){
                    requestStatus.postValue(medicationsParse.status)
                }
                pendingMedications.postValue(medicationsParse.data as MutableList<Medication>?)
            }

        }
    }

    fun addMedication(type:String,name:String,dose:String,date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.addMedication(token,type,name,dose,date) }
        }
    }

    fun editMedicationType(id: String, type:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.editMedicationType(token,id,type) }
        }
    }

    fun editMedicationName(id: String, name:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.editMedicationName(token,id,name) }
        }
    }

    fun editMedicationDose(id: String, dose:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.editMedicationDose(token,id,dose) }
        }
    }

    fun editMedicationDate(id: String, date:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.editMedicationDate(token,id,date) }
        }
    }

    fun editMedicationStatus(id: String, status:String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val resStatus = token?.let { repository.editMedicationStatus(token,id,status) }
            if (resStatus!=200){
                requestStatus.postValue(resStatus)
            }
        }
    }

    fun deleteMedication(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve updated data from the repository
            val updatedData = token?.let { repository.deleteMedication(token,id) }
        }
    }


}

// Extends the ViewModelProvider.Factory allowing us to control the viewmodel creation
// and provide the right parameters
class MedicationsViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicationsViewModel::class.java)) {
            return MedicationsViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}