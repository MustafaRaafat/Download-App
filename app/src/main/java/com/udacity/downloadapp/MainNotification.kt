package com.udacity.downloadapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.R

fun NotificationManager.sendNoti(mee: String, app: Context, status: String) {
    val contentIntent = Intent(app, DetailActivity::class.java)
    contentIntent.putExtra("name", mee)
    contentIntent.putExtra("status", status)
    val contentPendingIntent = PendingIntent.getActivity(
        app, 1,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val bulder = NotificationCompat.Builder(app, "channelId")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("done")
        .setContentText("download done")
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

    notify(1, bulder.build())
}

class MainNotification {

//    var builder=NotificationCompat.Builder(this)
}