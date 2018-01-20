package com.tasomaniac.devwidget.settings

import android.app.Activity
import android.app.TaskStackBuilder
import android.content.SharedPreferences
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.data.Analytics
import com.tasomaniac.devwidget.rx.SchedulingStrategy
import com.tasomaniac.devwidget.widget.WidgetUpdater
import javax.inject.Inject

class DisplaySettings @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val nightModePreferences: NightModePreferences,
    private val opacityPreferences: OpacityPreferences,
    private val widgetUpdater: WidgetUpdater,
    private val analytics: Analytics,
    private val scheduling: SchedulingStrategy,
    fragment: SettingsFragment
) : Settings(fragment), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun setup() {
        addPreferencesFromResource(R.xml.pref_display)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun release() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key.isKeyEquals(R.string.pref_key_night_mode)) {
            nightModePreferences.updateDefaultNightMode()
            activity.recreateTaskStack()

            updateAllWidgets()

            val selectedValue = nightModePreferences.mode.stringVale(context.resources)
            analytics.sendValueEvent("Night Mode", selectedValue)
        }
        if (key.isKeyEquals(R.string.pref_key_opacity)) {
            updateAllWidgets()

            val selectedValue = opacityPreferences.opacity.stringVale(context.resources)
            analytics.sendValueEvent("Opacity", selectedValue)
        }
    }

    private fun updateAllWidgets() {
        widgetUpdater.updateAll()
            .compose(scheduling.forCompletable())
            .subscribe()
    }

    private fun Activity.recreateTaskStack() {
        finish()
        TaskStackBuilder.create(this)
            .addNextIntentWithParentStack(intent)
            .startActivities()
    }
}
