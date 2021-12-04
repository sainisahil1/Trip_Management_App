package io.sahil.tripmanagementapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.sahil.tripmanagementapp.data.TripModel

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrip(trip: TripModel): Long?

    @Query("select * from trips")
    fun getTrips(): List<TripModel>
    //use pagination if want to handle large quantity

    @Query("select count(tripId) from trips")
    fun getCount(): Int

    @Query("select sum(durationInMs) from trips")
    fun getTotalDuration(): Long

    @Query("select * from trips where tripId=:id")
    fun getTripById(id: Long): TripModel

}