package io.sahil.tripmanagementapp.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.sahil.tripmanagementapp.data.TripModel
import io.sahil.tripmanagementapp.db.DatabaseHelper
import io.sahil.tripmanagementapp.io.TripIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel(private val myApplication: Application): AndroidViewModel(myApplication) {

    val tripLiveData = MutableLiveData<TripModel>()
    val toastLiveData = MutableLiveData<String>()

    fun fetchTrip(tripId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            val databaseHelper = DatabaseHelper.getInstance(myApplication.applicationContext)
            tripLiveData.postValue(databaseHelper.tripDao().getTripById(tripId))
        }
    }

    fun exportTrip(tripModel: TripModel){
        viewModelScope.launch(Dispatchers.IO) {
            TripIO.getInstance(myApplication).exportSingleTrip(tripModel).let {
                toastLiveData.postValue(it)
            }
        }
    }

}