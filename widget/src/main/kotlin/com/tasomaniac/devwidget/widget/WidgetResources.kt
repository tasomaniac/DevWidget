package com.tasomaniac.devwidget.widget

import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.tasomaniac.devwidget.data.Action
import com.tasomaniac.devwidget.data.Action.APP_DETAILS
import com.tasomaniac.devwidget.data.Action.PLAY_STORE
import com.tasomaniac.devwidget.data.Action.UNINSTALL
import com.tasomaniac.devwidget.settings.NightMode.OFF
import com.tasomaniac.devwidget.settings.NightMode.ON
import com.tasomaniac.devwidget.settings.NightModePreferences
import com.tasomaniac.devwidget.settings.Opacity
import javax.inject.Inject

class WidgetResources @Inject constructor(
    private val nightModePreferences: NightModePreferences,
    private val resources: Resources
) {

    val deleteIcon
        @DrawableRes get() = when (nightModePreferences.mode) {
            OFF -> R.drawable.ic_delete
            ON -> R.drawable.ic_delete_light
        }

    val settingsIcon
        @DrawableRes get() = when (nightModePreferences.mode) {
            OFF -> R.drawable.ic_settings
            ON -> R.drawable.ic_settings_light
        }

    val moreActionsIcon
        @DrawableRes get() = when (nightModePreferences.mode) {
            OFF -> R.drawable.ic_more_actions
            ON -> R.drawable.ic_more_actions_light
        }

    val playStoreIcon
        @DrawableRes get() = when (nightModePreferences.mode) {
            OFF -> R.drawable.ic_play_store
            ON -> R.drawable.ic_play_store_light
        }

    val devOptionsIcon
        @DrawableRes get() = when (nightModePreferences.mode) {
            OFF -> R.drawable.ic_dev_options
            ON -> R.drawable.ic_dev_options_light
        }

    val refreshIcon
        @DrawableRes get() = when (nightModePreferences.mode) {
            OFF -> R.drawable.ic_refresh
            ON -> R.drawable.ic_refresh_light
        }

    private val foregroundDark = ResourcesCompat.getColor(resources, R.color.foregroundDark, null)
    private val foregroundLight = ResourcesCompat.getColor(resources, R.color.foregroundLight, null)

    val foregroundColor
        @ColorInt get() = when (nightModePreferences.mode) {
            OFF -> foregroundDark
            ON -> foregroundLight
        }

    val foregroundColorInverse
        @ColorInt get() = when (nightModePreferences.mode) {
            OFF -> foregroundLight
            ON -> foregroundDark
        }

    @ColorInt @Suppress("MagicNumber")
    fun resolveBackgroundColor(opacity: Opacity): Int {
        val backgroundColor = foregroundColorInverse
        val opacityInt = opacity.stringVale(resources).toInt()
        return backgroundColor and 0xffffff or (opacityInt * 255 / 100 shl 24)
    }

    @Dimension
    fun resolveAppIconSize(widgetWidth: Int): Int {
        return when {
            widgetWidth < TINY_WIDGET_LIMIT -> 0
            widgetWidth < FAV_ACTION_LIMIT -> resources.getDimensionPixelSize(R.dimen.app_widget_icon_size_small)
            else -> resources.getDimensionPixelSize(R.dimen.app_widget_icon_size)
        }
    }

    fun resolveFavIcon(favAction: Action) = when (favAction) {
        UNINSTALL -> deleteIcon
        APP_DETAILS -> settingsIcon
        PLAY_STORE -> playStoreIcon
    }

    fun shouldDisplayFavAction(widgetWidth: Int): Boolean {
        return widgetWidth >= FAV_ACTION_LIMIT
    }

    fun shouldDisplayExtendedHeader(widgetWidth: Int): Boolean {
        return widgetWidth >= TINY_WIDGET_LIMIT
    }

    companion object {
        private const val TINY_WIDGET_LIMIT = 214
        private const val FAV_ACTION_LIMIT = 291
    }
}
