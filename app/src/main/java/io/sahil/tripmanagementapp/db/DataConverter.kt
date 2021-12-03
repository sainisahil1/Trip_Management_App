package io.sahil.tripmanagementapp.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.sahil.tripmanagementapp.data.LocationModel

class DataConverter {

    @TypeConverter
    fun toList(string: String): List<LocationModel>{
        val listType = object: TypeToken<List<LocationModel>>(){}.type
        return Gson().fromJson(string, listType)
    }

    @TypeConverter
    fun toJson(list: List<LocationModel>): String{
        return Gson().toJson(list)
    }

}