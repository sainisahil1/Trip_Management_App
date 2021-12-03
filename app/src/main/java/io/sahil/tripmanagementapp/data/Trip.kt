package io.sahil.tripmanagementapp.data

data class Trip(
    //val tripId: String,
    val startTime: String,
    var endTime: String,
    val locationArray: ArrayList<LocationModel>,
    var tripDuration: String,
    val displayTime: String,
    val startTimeStamp: Long,
    var endTimeStamp: Long
)
