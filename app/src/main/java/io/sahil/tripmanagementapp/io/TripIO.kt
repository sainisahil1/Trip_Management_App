package io.sahil.tripmanagementapp.io

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import io.sahil.tripmanagementapp.BuildConfig
import io.sahil.tripmanagementapp.data.TripModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class TripIO private constructor(private val application: Application){

    private val tag = TripIO::class.java.simpleName

    companion object{

        private var instance: TripIO? = null

        @Synchronized
        fun getInstance(application: Application): TripIO{
            if (instance == null){
                instance = TripIO(application)
            }
            return instance!!
        }

    }

    fun exportSingleTrip(tripModel: TripModel): String{
        val filename = getFileName(tripModel.tripId.toString())
        val file = File(application.filesDir, filename)
        if (file.createNewFile()) {
            Log.e(tag, "File created!")
        } else {
            Log.e(tag, "File already exist")
        }
        val fileWriter = FileWriter(file, false)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(convertToJson(tripModel).toString(2))
        bufferedWriter.close()
        fileWriter.close()
        Log.e(tag, file.absolutePath)
        return file.absolutePath
    }


    fun exportAllTrips(tripList: List<TripModel>): String{
        val filename = "trips-${Date()}.json"
        val file = File(application.filesDir, filename)
        if (file.createNewFile()) {
            Log.e(tag, "File created!")
        } else {
            Log.e(tag, "File already exist")
        }

        val jsonArray = JSONArray()
        for(item in tripList){
            jsonArray.put(convertToJson(item))
        }

        val fileWriter = FileWriter(file, false)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonArray.toString(2))
        bufferedWriter.close()
        fileWriter.close()
        Log.e(tag, file.absolutePath)
        return file.absolutePath
    }


    private fun getFileName(id: String): String {
        val suffix: String = if (BuildConfig.DEBUG) {
            "-qa"
        } else {
            "-prod"
        }
        return "trip-$id$suffix.json"
    }

    private fun convertToJson(tripModel: TripModel): JSONObject{
        val jsonObject = JSONObject()
        jsonObject.put("trip_id", tripModel.tripId.toString())
        jsonObject.put("start_time", tripModel.startTime)
        jsonObject.put("end_time", tripModel.endTime)

        val locationArray = JSONArray()
        for (item in tripModel.locationArray!!){
            val itemObject = JSONObject()
            itemObject.put("latitude", item.latitude.toString())
            itemObject.put("longitude", item.longitude.toString())
            itemObject.put("timestamp", item.timeStamp)
            itemObject.put("accuracy", item.accuracy.toString())
            locationArray.put(itemObject)
        }

        jsonObject.put("locations", locationArray)
        return jsonObject
    }


}