package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass:Application() {
    companion object{
        const val CHANNEL_ID = "channel1"
        const val PLAY = "play"
        const val NEXT = "next"
        const val PREVIOUS = "previous"
        const val EXIT = "exit"
    }
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, "এতিয়া গান বজোৱা হৈছে", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "গান দেখুওৱাৰ বাবে এইটো এটা গুৰুত্বপূৰ্ণ চেনেল!!"
            //for lockscreen -> test this and let me know.
//            notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
//            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}