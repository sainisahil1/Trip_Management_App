package io.sahil.tripmanagementapp.utils

import android.content.Context
import android.content.SharedPreferences

class MyPreferences(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("TRIP_MANAGEMENT_APP", Context.MODE_PRIVATE)

    private val TOTAL_TIME = "TOTAL_TIME"
    private val TOTAL_TRIPS = "TOTAL_TRIPS"

    fun saveTotalTime(timeInMilliSec: Long){
        val editor = sharedPreferences.edit()
        editor.putLong(TOTAL_TIME, timeInMilliSec)
        editor.apply()
    }

    fun getTotalTime(): Long{
        return sharedPreferences.getLong(TOTAL_TIME, 0)
    }


}