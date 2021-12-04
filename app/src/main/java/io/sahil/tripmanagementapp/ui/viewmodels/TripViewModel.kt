package io.sahil.tripmanagementapp.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.sahil.tripmanagementapp.data.TripModel
import io.sahil.tripmanagementapp.db.DatabaseHelper
import io.sahil.tripmanagementapp.io.TripIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripViewModel(private val myApplication: Application): AndroidViewModel(myApplication) {

    val tripListLiveData = MutableLiveData<List<TripModel>>()
    val toastLiveData = MutableLiveData<String>()

    fun fetchTrips(){
        viewModelScope.launch(Dispatchers.IO){
            val databaseHelper = DatabaseHelper.getInstance(myApplication.applicationContext)
            tripListLiveData.postValue(databaseHelper.tripDao().getTrips())
        }
    }

    fun exportTrips(tripList: List<TripModel>){
        viewModelScope.launch(Dispatchers.IO) {
            TripIO.getInstance(myApplication).exportAllTrips(tripList).let {
                toastLiveData.postValue(it)
            }
        }
    }


}