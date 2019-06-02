package com.tasomaniac.devwidget.settings

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.tasomaniac.devwidget.R

enum class NightMode(
    @StringRes override val value: Int,
    @StringRes override val entry: Int,
    val delegate: Int
) : PreferenceEntries {

    OFF(
        R.string.pref_value_night_mode_off,
        R.string.pref_entry_night_mode_off,
        MODE_NIGHT_NO
    ),
    ON(
        R.string.pref_value_night_mode_on,
        R.string.pref_entry_night_mode_on,
        MODE_NIGHT_YES
    ),
    BATTERY(
        R.string.pref_value_night_mode_battery,
        R.string.pref_entry_night_mode_battery,
        MODE_NIGHT_AUTO_BATTERY
    ),
    SYSTEM(
        R.string.pref_value_night_mode_system,
        R.string.pref_entry_night_mode_system,
        MODE_NIGHT_FOLLOW_SYSTEM
    );
}
