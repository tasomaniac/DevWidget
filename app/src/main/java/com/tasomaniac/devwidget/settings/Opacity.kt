package com.tasomaniac.devwidget.settings

import androidx.annotation.StringRes
import com.tasomaniac.devwidget.R

enum class Opacity(
    @StringRes override val value: Int,
    @StringRes override val entry: Int
) : PreferenceEntries {

    FULLY_TRANSPARENT(
        R.string.pref_value_opacity_fully_transparent,
        R.string.pref_entry_opacity_fully_transparent
    ),
    VISIBLE_10(R.string.pref_value_opacity_10, R.string.pref_entry_opacity_10),
    VISIBLE_25(R.string.pref_value_opacity_25, R.string.pref_entry_opacity_25),
    VISIBLE_50(R.string.pref_value_opacity_50, R.string.pref_entry_opacity_50),
    VISIBLE_75(R.string.pref_value_opacity_75, R.string.pref_entry_opacity_75),
    FULLY_OPAQUE(
        R.string.pref_value_opacity_fully_opaque,
        R.string.pref_entry_opacity_fully_opaque
    ),
}
