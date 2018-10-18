package com.tasomaniac.devwidget.data.updater

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_MIN
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import androidx.core.content.getSystemService
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.widget.toPendingActivity
import java.util.concurrent.TimeUnit

@TargetApi(Build.VERSION_CODES.O)
class WidgetRefreshService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    private val packageAddedReceiver = PackageAddedReceiver()
    private val packageRemovedReceiver = PackageRemovedReceiver()

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()
        startForeground()
        scheduleWidgetRefreshPeriodicJob()

        val packageAddedFilter = IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
            addDataScheme("package")
        }
        registerReceiver(packageAddedReceiver, packageAddedFilter)

        val packageRemovedFilter = IntentFilter(Intent.ACTION_PACKAGE_REMOVED).apply {
            addDataScheme("package")
        }
        registerReceiver(packageRemovedReceiver, packageRemovedFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(packageAddedReceiver)
        unregisterReceiver(packageRemovedReceiver)
    }

    private fun setupNotificationChannel() {
        val notificationManager = getSystemService<NotificationManager>()!!
        val channelName = getString(R.string.widget_refresh_notification_channel_name)
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_MIN).apply {
            lockscreenVisibility = Notification.VISIBILITY_SECRET
            setShowBadge(false)
            description = getString(R.string.widget_refresh_notification_channel_desc)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun startForeground() {
        val stopAction = Notification.Action.Builder(
            Icon.createWithResource(this, R.drawable.empty),
            getString(R.string.stop),
            Intent(this, StopWidgetRefreshActivity::class.java).toPendingActivity(this)
        ).build()
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.widget_refresh_notification_content))
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setColor(getColor(R.color.theme_primary))
            .addAction(stopAction)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun scheduleWidgetRefreshPeriodicJob() {
        val jobScheduler = getSystemService<JobScheduler>()!!
        val componentName = ComponentName(this, WidgetRefreshRescheduleJob::class.java)
        jobScheduler.schedule(
            JobInfo.Builder(RESCHEDULE_JOB_ID, componentName)
                .setRequiresBatteryNotLow(true)
                .setPeriodic(TimeUnit.HOURS.toMillis(2))
                .build()
        )
    }

    companion object {
        private const val RESCHEDULE_JOB_ID = 1
        private const val NOTIFICATION_ID = 100
        private const val CHANNEL_ID = "widgetRefreshService"
    }
}
