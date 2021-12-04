package io.sahil.tripmanagementapp.ui.services

import android.Manifest
import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.Build

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log

import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import io.sahil.tripmanagementapp.data.LocationModel
import io.sahil.tripmanagementapp.data.Trip
import io.sahil.tripmanagementapp.data.TripModel
import io.sahil.tripmanagementapp.db.DatabaseHelper
import io.sahil.tripmanagementapp.utils.MyPreferences
import io.sahil.tripmanagementapp.utils.MyUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import io.sahil.tripmanagementapp.R
import android.graphics.BitmapFactory

import androidx.core.app.NotificationCompat

import io.sahil.tripmanagementapp.ui.activity.MainActivity
import androidx.core.app.NotificationManagerCompat









class ForegroundService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private val NOTIFICATION_ID = 1000
    private val PENDING_INTENT_REQUEST_CODE = 1001
    private val CHANNEL_NAME = "TRIP_CHANNEL"
    private val CHANNEL_ID = "TRIP_CHANNEL_ID"

    private var isRunning = false
    private lateinit var myPreferences: MyPreferences
    private var trip: Trip? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val locationArrayList = arrayListOf<LocationModel>()



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        myPreferences = MyPreferences(applicationContext)

        if (isRunning){
            //this will handle edge case where start trip may be called even when trip is running
            myPreferences.saveIsTripRunning(true)
            updateIndicator()
        } else {
            showForegroundNotification()
            startTrip()
        }

        registerReceiver()

        return START_STICKY
    }


    private fun showForegroundNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                PENDING_INTENT_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(this)

            val bigTextStyle = NotificationCompat.BigTextStyle()
            bigTextStyle.bigText("Trip running..")
            bigTextStyle.setBigContentTitle("Trip Management App")

            builder.setStyle(bigTextStyle)
            builder.setOngoing(true)
            // builder.setOnlyAlertOnce(true); //to quietly update the notification
            // builder.setOnlyAlertOnce(true); //to quietly update the notification
            builder.setWhen(System.currentTimeMillis())
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
            builder.setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.ic_launcher_round
                )
            )
            builder.priority = Notification.PRIORITY_HIGH
            builder.setFullScreenIntent(pendingIntent, true)

            val notification = builder.build()
            startForeground(NOTIFICATION_ID, notification)

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelID: String, channelName: String) {
        val resultIntent = Intent(this, MainActivity::class.java)
        val taskStackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        taskStackBuilder.addNextIntentWithParentStack(resultIntent)
        val pendingIntent: PendingIntent = taskStackBuilder.getPendingIntent(
            PENDING_INTENT_REQUEST_CODE,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationChannel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val notificationManager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        notificationManager.createNotificationChannel(notificationChannel)
        val notificationBuilder = NotificationCompat.Builder(this, channelID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Trip Management App")
            .setContentText("Trip running..")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build())
        startForeground(NOTIFICATION_ID, notification)
    }


    private val tag = ForegroundService::class.java.simpleName


    private fun startTracking(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            val locationRequest = LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 5000
                smallestDisplacement = 2F
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
    }

    private fun stopTracking(){
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        isRunning = false
        myPreferences.saveIsTripRunning(false)
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(receiver)
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations){
                location?.let {
                    if (it.accuracy < 100){
                        val locationModel = LocationModel(it.latitude, it.longitude, MyUtils.parseTime(Date()), it.accuracy.toDouble())
                        locationArrayList.add(locationModel)
                        Log.e(tag, locationModel.toString())
                    }
                }
            }
        }
    }



    private fun startTrip(){
        myPreferences.saveIsTripRunning(true)
        startTracking()
        val startTime = Date()
        trip = Trip(
            MyUtils.parseTime(startTime),
            "",
            locationArrayList,
            "",
            MyUtils.getReadableDate(startTime),
            startTime.time,
            0L
        )
        isRunning = true
        updateIndicator()
    }

    private fun stopTrip(){
        stopTracking()
        myPreferences.saveIsTripRunning(false)
        val endTime = Date()
        trip?.let {
            it.endTime = MyUtils.parseTime(endTime)
            val timeDiffInMs = endTime.time - it.startTimeStamp
            it.tripDuration = MyUtils.getTimeInHM(timeDiffInMs)
            CoroutineScope(Dispatchers.IO).launch {
                DatabaseHelper.getInstance(applicationContext).tripDao().insertTrip(TripModel(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    locationArray = it.locationArray.toList(),
                    tripDuration = it.tripDuration,
                    displayTime = it.displayTime,
                    durationInMs = timeDiffInMs
                ))
            }
        }
        updateIndicator()
        onDestroy()
    }


    private fun registerReceiver(){
        val intentFilter = IntentFilter()
        intentFilter.addAction("STOP_TRIP")
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(receiver, intentFilter)
    }

    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.let {
                when(it.action){
                    "STOP_TRIP" -> stopTrip()
                }
            }
        }
    }

    private fun updateIndicator(){
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent("UPDATE_INDICATOR"))
    }



}