package com.tasomaniac.devwidget.widget

import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.settings.NightMode.OFF
import com.tasomaniac.devwidget.settings.NightMode.ON
import com.tasomaniac.devwidget.settings.NightModePreferences
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

    @Dimension
    fun resolveAppIconSize(widgetWidth: Int): Int {
        return when {
            widgetWidth < TINY_WIDGET_LIMIT -> 0
            widgetWidth < FAV_ACTION_LIMIT -> resources.getDimensionPixelSize(R.dimen.app_widget_icon_size_small)
            else -> resources.getDimensionPixelSize(R.dimen.app_widget_icon_size)
        }
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
