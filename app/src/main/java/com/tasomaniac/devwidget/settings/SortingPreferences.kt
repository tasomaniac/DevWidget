package com.tasomaniac.devwidget.settings

import android.content.SharedPreferences
import android.content.res.Resources
import com.tasomaniac.devwidget.R
import javax.inject.Inject

class SortingPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources
) {

    private val key = resources.getString(R.string.pref_key_sorting)

    val sorting: Sorting
        get() {
            val value = sharedPreferences.getString(key, null)
            return PreferenceEntries.fromValue(resources, value) ?: Sorting.ORDER_ADDED
        }

}
