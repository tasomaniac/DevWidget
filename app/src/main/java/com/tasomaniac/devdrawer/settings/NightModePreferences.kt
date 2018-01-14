package com.tasomaniac.devdrawer.settings

import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatDelegate
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.settings.NightMode.OFF
import com.tasomaniac.devdrawer.settings.NightMode.ON
import javax.inject.Inject

class NightModePreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources) {

  private val key = resources.getString(R.string.pref_key_night_mode)

  val mode: NightMode
    get() {
      val value = sharedPreferences.getString(key, null)
      return PreferenceEntries.fromValue(resources, value) ?: ON
    }

  fun updateDefaultNightMode() {
    AppCompatDelegate.setDefaultNightMode(mode.delegate)
  }

}
