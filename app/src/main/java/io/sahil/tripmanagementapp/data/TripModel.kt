package io.sahil.tripmanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.sahil.tripmanagementapp.db.DataConverter


@Entity(tableName = "trips")
data class TripModel(
    @PrimaryKey(autoGenerate = true)
    val tripId: Long? = null,
    val startTime: String,
    val endTime: String,
    @TypeConverters(DataConverter::class)
    val locationArray: List<LocationModel>?,
    val tripDuration: String,
    val durationInMs: Long,
    val displayTime: String
)
