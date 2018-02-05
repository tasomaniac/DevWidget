package com.tasomaniac.devwidget.widget

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.StringRes
import android.widget.Toast
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity

class ClickHandlingActivity : Activity() {

    private val launchWhat
        get() = intent.getStringExtra(LAUNCH_WHAT)

    private val extraPackageName
        get() = intent.getStringExtra(EXTRA_PACKAGE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (launchWhat) {
            LAUNCH_APP -> {
                ActivityChooserActivity.createIntent(this, extraPackageName)
                    .start()
//                packageManager.getLaunchIntentForPackage(extraPackageName)?.apply {
//                    addCategory(Intent.CATEGORY_LAUNCHER)
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
//                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
//                }?.start() ?: toast(R.string.widget_error_activity_not_found)
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

    private fun toast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val LAUNCH_WHAT = "LAUNCH_WHAT"
        private const val LAUNCH_APP = "LAUNCH_APP"
        private const val UNINSTALL_APP = "UNINSTALL_APP"
        private const val APP_DETAILS = "APP_DETAILS"

        private const val EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME"

        fun createForLaunchApp(packageName: String) = Intent().apply {
            putExtra(LAUNCH_WHAT, LAUNCH_APP)
            putExtra(EXTRA_PACKAGE_NAME, packageName)
        }

        fun createForUninstallApp(packageName: String) = Intent().apply {
            putExtra(LAUNCH_WHAT, UNINSTALL_APP)
            putExtra(EXTRA_PACKAGE_NAME, packageName)
        }

        fun createForAppDetails(packageName: String) = Intent().apply {
            putExtra(LAUNCH_WHAT, APP_DETAILS)
            putExtra(EXTRA_PACKAGE_NAME, packageName)
        }

        fun intent(context: Context) = Intent(context, ClickHandlingActivity::class.java)

    }
}
