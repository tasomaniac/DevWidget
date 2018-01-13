package com.tasomaniac.devdrawer.settings

import android.support.v4.app.ShareCompat
import android.support.v7.preference.Preference
import com.tasomaniac.devdrawer.BuildConfig
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Analytics
import javax.inject.Inject

class OtherSettings @Inject constructor(
    fragment: SettingsFragment,
    private val analytics: Analytics
) : Settings(fragment) {

  override fun setup() {
    addPreferencesFromResource(R.xml.pref_others)

    findPreference(R.string.pref_key_open_source).onPreferenceClickListener = onPreferenceClickListener
    findPreference(R.string.pref_key_contact).onPreferenceClickListener = onPreferenceClickListener
    setupVersionPreference()
  }

  private val onPreferenceClickListener = Preference.OnPreferenceClickListener {
    when {
      it.isKeyEquals(R.string.pref_key_open_source) -> displayLicensesDialogFragment()
      it.isKeyEquals(R.string.pref_key_contact) -> startContactEmailChooser()
    }

    analytics.sendEvent("Preference", "Item Click", it.key)
    true
  }

  private fun setupVersionPreference() {
    val version = StringBuilder(BuildConfig.VERSION_NAME)
    if (BuildConfig.DEBUG) {
      version.append(" (")
          .append(BuildConfig.VERSION_CODE)
          .append(")")
    }
    val preference = findPreference(R.string.pref_key_version)
    preference.summary = version
  }

  private fun displayLicensesDialogFragment() {
    LicensesDialogFragment.newInstance().show(activity.supportFragmentManager, "LicensesDialog")
  }

  private fun startContactEmailChooser() {
    ShareCompat.IntentBuilder.from(activity)
        .addEmailTo("Said Tahsin Dane <tasomaniac+openlinkwith@gmail.com>")
        .setSubject(context.getString(R.string.app_name))
        .setType("message/rfc822")
        .startChooser()
  }

}
