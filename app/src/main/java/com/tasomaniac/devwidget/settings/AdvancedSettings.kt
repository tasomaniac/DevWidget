package com.tasomaniac.devwidget.settings

import android.content.SharedPreferences
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.data.Analytics
import javax.inject.Inject

class AdvancedSettings @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val clickBehaviorPreferences: ClickBehaviorPreferences,
    private val analytics: Analytics,
    fragment: SettingsFragment
) : Settings(fragment),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun setup() {
        addPreferencesFromResource(R.xml.pref_advanced)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun release() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key.isKeyEquals(R.string.pref_key_click_behavior)) {

            val selectedValue = clickBehaviorPreferences.clickBehavior.stringVale(context.resources)
            analytics.sendValueEvent("ClickBehavior", selectedValue)
        }
    }
}
