package io.sahil.tripmanagementapp.utils

import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap
import android.graphics.Canvas

import android.graphics.drawable.Drawable




class MyUtils {

    companion object{

        fun parseTime(date: Date): String{
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault())
            val dateString = simpleDateFormat.format(date)
            Log.e("TIME_UTIL", dateString)
            return dateString
        }

        fun getReadableDate(date: Date): String{
            val simpleDateFormat = SimpleDateFormat("MMM dd yyyy, hh:mm", Locale.getDefault())
            return simpleDateFormat.format(date)
        }

        fun getTimeInHM(totalMs: Long): String {
            Log.e("TIME_UTIL", totalMs.toString())
            val totalSecs = totalMs/1000
            val diffMinutes = (totalSecs % 3600) / 60
            val diffHours = totalSecs / 3600
            return if (diffHours == 0L){
                "$diffMinutes min"
            } else {
                "$diffHours hr $diffMinutes min"
            }
        }

        fun timeStringToDate(string: String): Date?{
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault())
            return try {
                simpleDateFormat.parse(string)
            } catch (e: Exception){
                e.printStackTrace()
                null
            }
        }


        fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
            val bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, widthPixels, heightPixels)
            drawable.draw(canvas)
            return bitmap
        }


    }

}