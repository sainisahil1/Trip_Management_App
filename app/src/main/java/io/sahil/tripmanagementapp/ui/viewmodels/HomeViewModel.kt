package io.sahil.tripmanagementapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.sahil.tripmanagementapp.db.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val myApplication: Application): AndroidViewModel(myApplication) {

    //using AndroidViewModel to get rid of memory leak of context

    val totalTripsLiveData = MutableLiveData<String>()
    val totalDurationLiveData = MutableLiveData<Long>()

    private val tag = HomeViewModel::class.java.simpleName

    fun fetchTripsCount(){
        viewModelScope.launch(Dispatchers.IO){
            Log.e(tag, "fetch trips count")
            val databaseHelper = DatabaseHelper.getInstance(myApplication.applicationContext)
            totalTripsLiveData.postValue(databaseHelper.tripDao().getCount().toString())
        }
    }

    fun fetchTotalDuration(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.e(tag, "fetch total duration")
            val databaseHelper = DatabaseHelper.getInstance(myApplication.applicationContext)
            totalDurationLiveData.postValue(databaseHelper.tripDao().getTotalDuration())
        }
    }

}