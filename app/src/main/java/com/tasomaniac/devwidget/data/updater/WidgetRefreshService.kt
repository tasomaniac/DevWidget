package com.tasomaniac.devwidget.data.updater

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.content.getSystemService
import com.tasomaniac.devwidget.R

@TargetApi(Build.VERSION_CODES.O)
class WidgetRefreshService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    private val packageAddedReceiver = PackageAddedReceiver()
    private val packageRemovedReceiver = PackageRemovedReceiver()

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Widget update service is on")
            .setSmallIcon(R.drawable.ic_delete_light)
            .build()
        startForeground(NOTIFICATION_ID, notification)

        val packageAddedFilter = IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply { addDataScheme("package") }
        registerReceiver(packageAddedReceiver, packageAddedFilter)

        val packageRemovedFilter = IntentFilter(Intent.ACTION_PACKAGE_REMOVED).apply { addDataScheme("package") }
        registerReceiver(packageRemovedReceiver, packageRemovedFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(packageAddedReceiver)
        unregisterReceiver(packageRemovedReceiver)
    }

    private fun setupNotificationChannel() {
        val notificationManager = getSystemService<NotificationManager>()!!
        val channel = NotificationChannel(CHANNEL_ID, "Widget updater", NotificationManager.IMPORTANCE_MIN)
        channel.lockscreenVisibility = Notification.VISIBILITY_SECRET
        channel.description =
                "Android Oreo requires a persistent background service for widget auto-update. Disabling will require to use refresh button manually."
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 100
        private const val CHANNEL_ID = "widgetRefreshService"
    }
}
