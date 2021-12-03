package io.sahil.tripmanagementapp.data

data class LocationModel(
    val latitude: Double,
    val longitude: Double,
    val timeStamp: String,
    val accuracy: Double
){
    override fun toString(): String {
        return "Lat: $latitude, Lng: $longitude, timestamp: $timeStamp, accuracy: $accuracy"
    }
}
