package com.tasomaniac.devwidget.widget.click

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.tasomaniac.devwidget.navigation.FinishCommand
import com.tasomaniac.devwidget.navigation.Navigator
import com.tasomaniac.devwidget.widget.R
import com.tasomaniac.devwidget.widget.click.commands.ActivityChooserCommand
import com.tasomaniac.devwidget.widget.click.commands.UninstallCommand
import javax.inject.Inject

internal class ClickHandlingNavigation @Inject constructor(
    private val actionListGenerator: ActionListGenerator,
    private val activity: FragmentActivity,
    private val navigator: Navigator,
    private val input: ClickHandlingActivity.Input
) {

    fun navigateToChooser() =
        navigator.navigate(ActivityChooserCommand(input.packageName, input.user) and FinishCommand)

    fun uninstall() = navigator.navigate(UninstallCommand(input.packageName) and FinishCommand)

    fun navigateToActionsDialog() {
        val adapter = ActionDialogAdapter(
            activity,
            actionListGenerator.actionList()
        )
        AlertDialog.Builder(activity)
            .setTitle(R.string.widget_choose_action)
            .setAdapter(adapter) { _, position ->
                val action = adapter.getItem(position)!!
                val command = action.commandForPackage?.invoke(input.packageName) ?: action.command!!
                navigator.navigate(command)
            }
            .setOnDismissListener {
                navigator.navigate(FinishCommand)
            }
            .show()
    }
}
