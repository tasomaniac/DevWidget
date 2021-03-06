package com.tasomaniac.devwidget.widget.click

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.UserHandle
import com.tasomaniac.devwidget.data.Action
import com.tasomaniac.devwidget.widget.DisplayApplicationInfo
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

internal class ClickHandlingActivity : DaggerAppCompatActivity() {

    @Inject lateinit var navigation: ClickHandlingNavigation
    @Inject lateinit var input: Input

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (input.launchWhat) {
            LAUNCH_APP -> {
                navigation.navigateToChooser()
            }
            FAV_ACTION -> {
                navigation.navigateToAction(intent.favAction!!)
            }
            ACTIONS_DIALOG -> {
                navigation.navigateToActionsDialog()
            }
        }
    }

    @Parcelize
    data class Input(
        val launchWhat: String,
        val packageName: String,
        val user: UserHandle
    ) : Parcelable

    companion object {

        private const val LAUNCH_APP = "LAUNCH_APP"
        private const val FAV_ACTION = "FAV_ACTION"
        private const val ACTIONS_DIALOG = "ACTIONS_DIALOG"

        fun createForLaunchApp(appInfo: DisplayApplicationInfo) = intentFor(LAUNCH_APP, appInfo)
        fun createForFavAction(appInfo: DisplayApplicationInfo, favAction: Action) =
            intentFor(FAV_ACTION, appInfo).apply {
                this.favAction = favAction
            }

        fun createForActionsDialog(appInfo: DisplayApplicationInfo) = intentFor(ACTIONS_DIALOG, appInfo)

        private fun intentFor(launchWhat: String, appInfo: DisplayApplicationInfo) = Intent().apply {
            input = Input(launchWhat, appInfo.packageName, appInfo.user)
        }

        fun intent(context: Context) = Intent(context, ClickHandlingActivity::class.java)
    }
}

private const val EXTRA_INPUT = "EXTRA_INPUT"
private const val EXTRA_ACTION = "EXTRA_ACTION"

internal var Intent.input: ClickHandlingActivity.Input
    get() = getParcelableExtra(EXTRA_INPUT)
    set(value) {
        putExtra(EXTRA_INPUT, value)
    }

internal var Intent.favAction: Action?
    get() = getParcelableExtra(EXTRA_ACTION)
    set(value) {
        putExtra(EXTRA_ACTION, value as Parcelable)
    }
