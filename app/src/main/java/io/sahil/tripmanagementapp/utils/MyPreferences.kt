package io.sahil.tripmanagementapp.utils

import android.content.Context

class MyPreferences(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("TRIP_MANAGEMENT_APP", Context.MODE_PRIVATE)

    private val TOTAL_TIME = "TOTAL_TIME"
    private val TOTAL_TRIPS = "TOTAL_TRIPS"
    private val IS_TRIP_RUNNING = "IS_TRIP_RUNNING"

   /* fun addTripTimeToTotal(timeInMilliSec: Long){
        val currentTotal  = getTotalTime()
        val editor = sharedPreferences.edit()
        editor.putLong(TOTAL_TIME, currentTotal + timeInMilliSec)
        editor.apply()
    }

    fun getTotalTime(): Long{
        return sharedPreferences.getLong(TOTAL_TIME, 0)
    }*/

    fun saveIsTripRunning(isTripRunning: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_TRIP_RUNNING, isTripRunning)
        editor.apply()
    }

    fun getIsTripRunning(): Boolean{
        return sharedPreferences.getBoolean(IS_TRIP_RUNNING, false)
    }


}