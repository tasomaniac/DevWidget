package com.tasomaniac.devwidget.widget.click

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import androidx.core.widget.toast
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity
import javax.inject.Inject

class ClickHandlingNavigation @Inject constructor(
    private val activity: Activity,
    private val input: ClickHandlingActivity.Input
) {

    fun navigateToChooser() {
        ActivityChooserActivity.createIntent(activity, input.packageName, input.user)
            .start()
    }

    fun uninstall() {
        Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
            data = Uri.parse("package:${input.packageName}")
        }.start()
    }

    fun navigateToAppDetails() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${input.packageName}")
        }.start()
    }

    fun navigateToActionsDialog() {
        val actions = arrayOf(
            activity.getString(R.string.widget_action_uninstall),
            activity.getString(R.string.widget_action_app_details)
        )
        AlertDialog.Builder(activity)
            .setTitle(R.string.widget_choose_action)
            .setItems(actions) { _, which -> navigate(which) }
            .setOnDismissListener {
                activity.finish()
            }
            .show()
    }

    private fun navigate(which: Int) {
        when (which) {
            0 -> uninstall()
            1 -> navigateToAppDetails()
        }
    }

    fun Intent.start() = activity.apply {
        try {
            startActivity(this@start)
        } catch (e: ActivityNotFoundException) {
            toast(R.string.widget_error_activity_cannot_be_launched)
        } finally {
            finish()
        }
    }
}
