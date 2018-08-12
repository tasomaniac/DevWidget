package com.tasomaniac.devwidget.settings

import androidx.annotation.StringRes
import com.tasomaniac.devwidget.R

enum class ClickBehavior(
    @StringRes override val value: Int,
    @StringRes override val entry: Int
) : PreferenceEntries {

    LAUNCHER(
        R.string.pref_value_click_behavior_launcher,
        R.string.pref_entry_click_behavior_launcher
    ),
    EXPORTED(
        R.string.pref_value_click_behavior_exported,
        R.string.pref_entry_click_behavior_exported
    ),

}
