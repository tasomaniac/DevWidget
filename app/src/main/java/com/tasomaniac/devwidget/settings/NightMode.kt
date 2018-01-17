package com.tasomaniac.devwidget.settings

import android.support.annotation.StringRes
import android.support.v7.app.AppCompatDelegate
import com.tasomaniac.devwidget.R

enum class NightMode(
    @StringRes override val value: Int,
    @StringRes override val entry: Int,
    val delegate: Int
) : PreferenceEntries {

    OFF(
        R.string.pref_value_night_mode_off,
        R.string.pref_entry_night_mode_off,
        AppCompatDelegate.MODE_NIGHT_NO
    ),
    ON(
        R.string.pref_value_night_mode_on,
        R.string.pref_entry_night_mode_on,
        AppCompatDelegate.MODE_NIGHT_YES
    );
}
