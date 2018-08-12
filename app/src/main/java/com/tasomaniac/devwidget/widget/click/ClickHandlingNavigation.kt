package com.tasomaniac.devwidget.widget.click

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import androidx.core.widget.toast
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.widget.WidgetResources
import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity
import javax.inject.Inject

class ClickHandlingNavigation @Inject constructor(
    private val widgetResources: WidgetResources,
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
        val adapter = ActionDialogAdapter(
            activity,
            listOf(
                Action(widgetResources.deleteIcon, R.string.widget_action_uninstall) {
                    uninstall()
                },
                Action(widgetResources.settingsIcon, R.string.widget_action_app_details) {
                    navigateToAppDetails()
                }
            )
        )
        AlertDialog.Builder(activity)
            .setTitle(R.string.widget_choose_action)
            .setAdapter(adapter) { _, position ->
                adapter.getItem(position)!!.navigate()
            }
            .setOnDismissListener {
                activity.finish()
            }
            .show()
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
