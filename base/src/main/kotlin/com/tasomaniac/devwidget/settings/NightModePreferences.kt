package com.tasomaniac.devwidget.settings

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatDelegate
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.settings.NightMode.OFF
import com.tasomaniac.devwidget.settings.NightMode.SYSTEM
import javax.inject.Inject

class NightModePreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources
) {

    private val key = resources.getString(R.string.pref_key_night_mode)

    val mode: NightMode
        get() {
            val value = sharedPreferences.getString(key, null)
            return PreferenceEntries.fromValue(resources, value) ?: defaultNightMode()
        }

    private fun defaultNightMode() = when {
        SDK_INT >= P -> SYSTEM
        else -> OFF
    }

    fun updateDefaultNightMode() {
        AppCompatDelegate.setDefaultNightMode(mode.delegate)
    }
}
