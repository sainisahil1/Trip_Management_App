package io.sahil.tripmanagementapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.sahil.tripmanagementapp.data.TripModel
import io.sahil.tripmanagementapp.db.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripViewModel(private val myApplication: Application): AndroidViewModel(myApplication) {

    val tripListLiveData = MutableLiveData<List<TripModel>>()

    fun fetchTrips(){
        viewModelScope.launch(Dispatchers.IO){
            val databaseHelper = DatabaseHelper.getInstance(myApplication)
            tripListLiveData.postValue(databaseHelper.tripDao().getTrips())
        }
    }


}