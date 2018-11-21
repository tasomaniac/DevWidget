package com.tasomaniac.devwidget.settings

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.settings.NightMode.OFF
import javax.inject.Inject

class NightModePreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources
) {

    private val key = resources.getString(R.string.pref_key_night_mode)

    val mode: NightMode
        get() {
            val value = sharedPreferences.getString(key, null)
            return PreferenceEntries.fromValue(resources, value) ?: OFF
        }

    fun updateDefaultNightMode() {
        AppCompatDelegate.setDefaultNightMode(mode.delegate)
    }
}
