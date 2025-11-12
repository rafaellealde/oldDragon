// service/NotificationChannelCreator.kt
package com.olddragon.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationChannelCreator {
    companion object {
        fun createAventuraChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "aventura_channel",
                    "Modo Aventura",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notificações do modo aventura do Old Dragon"
                }
                
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}