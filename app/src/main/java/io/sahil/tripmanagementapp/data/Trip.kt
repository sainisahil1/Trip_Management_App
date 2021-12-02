package io.sahil.tripmanagementapp.data

data class Trip(
    val tripId: String,
    val startTime: String,
    val endTime: String,
    val locationArray: ArrayList<LocationModel>,
    val tripDuration: String,
    val displayTime: String
)
