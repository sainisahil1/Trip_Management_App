package io.sahil.tripmanagementapp.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import io.sahil.tripmanagementapp.data.TripModel

@Database(entities = [TripModel::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class DatabaseHelper: RoomDatabase() {

    abstract fun tripDao(): TripDao

    companion object{

        private var instance: DatabaseHelper? = null

        //using singleton

        @Synchronized
        fun getInstance(context: Context): DatabaseHelper{
            if (instance == null){
                instance = Room
                    .databaseBuilder(context, DatabaseHelper::class.java, DatabaseHelper::class.java.simpleName)
                    .build()
            }
            return instance!!
        } //populate this


    }

}