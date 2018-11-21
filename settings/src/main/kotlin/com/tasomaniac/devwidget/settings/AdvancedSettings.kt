package com.tasomaniac.devwidget.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.lifecycle.LifecycleOwner
import androidx.preference.SwitchPreference
import com.tasomaniac.devwidget.data.Analytics
import com.tasomaniac.devwidget.data.updater.ShouldStartWidgetRefreshService
import com.tasomaniac.devwidget.data.updater.WidgetRefreshService
import javax.inject.Inject

class AdvancedSettings @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val clickBehaviorPreferences: ClickBehaviorPreferences,
    private val autoUpdatePreferences: AutoUpdatePreferences,
    private val shouldStartWidgetRefreshService: ShouldStartWidgetRefreshService,
    private val analytics: Analytics,
    fragment: SettingsFragment
) : Settings(fragment),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(owner: LifecycleOwner) {
        addPreferencesFromResource(R.xml.pref_advanced)

        if (SDK_INT < O) {
            val preference = findPreference(R.string.pref_key_auto_updater)
            preference.parent!!.removePreference(preference)
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when {
            key.isKeyEquals(R.string.pref_key_click_behavior) -> {
                val selectedValue = clickBehaviorPreferences.clickBehavior.stringVale(context.resources)
                analytics.sendValueEvent("ClickBehavior", selectedValue)
            }
            key.isKeyEquals(R.string.pref_key_auto_updater) -> autoUpdateToggled()
        }
    }

    private fun autoUpdateToggled() {
        val autoUpdate = autoUpdatePreferences.autoUpdate
        toggleWidgetRefreshService()
        (findPreference(R.string.pref_key_auto_updater) as SwitchPreference).isChecked = autoUpdate
        analytics.sendValueEvent("AutoUpdate", autoUpdate.toString())
    }

    private fun toggleWidgetRefreshService() {
        val intent = Intent(activity, WidgetRefreshService::class.java)
        if (SDK_INT >= O && shouldStartWidgetRefreshService.check()) {
            activity.startForegroundService(intent)
        } else {
            activity.stopService(intent)
        }
    }
}
