package com.tasomaniac.devwidget.widget

import android.content.res.Resources
import android.support.annotation.ColorInt
import android.support.annotation.Dimension
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
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
    fun resolveAppIconSize(minWidth: Int?): Int {
        return if (minWidth != null && minWidth < TINY_WIDGET_LIMIT) {
            resources.getDimensionPixelSize(R.dimen.app_widget_icon_size_small)
        } else {
            resources.getDimensionPixelSize(R.dimen.app_widget_icon_size)
        }
    }

    companion object {
        private const val TINY_WIDGET_LIMIT = 214
    }

}
