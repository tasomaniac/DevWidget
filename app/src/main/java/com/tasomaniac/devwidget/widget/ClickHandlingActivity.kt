package com.tasomaniac.devwidget.widget

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.UserHandle
import android.provider.Settings
import androidx.core.widget.toast
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity

class ClickHandlingActivity : Activity() {

    private val launchWhat
        get() = intent.getStringExtra(LAUNCH_WHAT)

    private val extraPackageName
        get() = intent.getStringExtra(EXTRA_PACKAGE_NAME)

    private val extraUser
        get() = intent.getParcelableExtra<UserHandle>(EXTRA_USER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (launchWhat) {
            LAUNCH_APP -> {
                ActivityChooserActivity.createIntent(this, extraPackageName, extraUser)
                    .start()
            }
            UNINSTALL_APP -> {
                Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
                    data = Uri.parse("package:$extraPackageName")
                }.start()
            }
            APP_DETAILS -> {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:$extraPackageName")
                }.start()
            }
        }

        finish()
    }

    fun Intent.start() =
        try {
            startActivity(this)
        } catch (e: ActivityNotFoundException) {
            toast(R.string.widget_error_activity_cannot_be_launched)
        }

    companion object {

        private const val LAUNCH_WHAT = "LAUNCH_WHAT"
        private const val LAUNCH_APP = "LAUNCH_APP"
        private const val UNINSTALL_APP = "UNINSTALL_APP"
        private const val APP_DETAILS = "APP_DETAILS"

        private const val EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME"
        private const val EXTRA_USER = "EXTRA_USER"

        fun createForLaunchApp(appInfo: DisplayApplicationInfo) = Intent().apply {
            putExtra(LAUNCH_WHAT, LAUNCH_APP)
            putExtra(EXTRA_PACKAGE_NAME, appInfo.packageName)
            putExtra(EXTRA_USER, appInfo.user)
        }

        fun createForUninstallApp(appInfo: DisplayApplicationInfo) = Intent().apply {
            putExtra(LAUNCH_WHAT, UNINSTALL_APP)
            putExtra(EXTRA_PACKAGE_NAME, appInfo.packageName)
            putExtra(EXTRA_USER, appInfo.user)
        }

        fun createForAppDetails(appInfo: DisplayApplicationInfo) = Intent().apply {
            putExtra(LAUNCH_WHAT, APP_DETAILS)
            putExtra(EXTRA_PACKAGE_NAME, appInfo.packageName)
            putExtra(EXTRA_USER, appInfo.user)
        }

        fun intent(context: Context) = Intent(context, ClickHandlingActivity::class.java)

    }
}
