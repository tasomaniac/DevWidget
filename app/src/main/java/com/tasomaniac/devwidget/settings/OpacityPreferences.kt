package com.tasomaniac.devwidget.settings

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.ColorInt
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.widget.WidgetResources
import javax.inject.Inject

class OpacityPreferences @Inject constructor(
    private val widgetResources: WidgetResources,
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources
) {

    private val key = resources.getString(R.string.pref_key_opacity)

    val opacity: Opacity
        get() {
            val value = sharedPreferences.getString(key, null)
            return PreferenceEntries.fromValue(resources, value) ?: Opacity.VISIBLE_50
        }

    val backgroundColor: Int
        @ColorInt @Suppress("MagicNumber") get() {
            val backgroundColor = widgetResources.foregroundColorInverse
            val opacity = opacity.stringVale(resources).toInt()
            return backgroundColor and 0xffffff or (opacity * 255 / 100 shl 24)
        }

}
