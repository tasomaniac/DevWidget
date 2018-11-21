package com.tasomaniac.devwidget.settings

import androidx.core.app.ShareCompat
import androidx.lifecycle.LifecycleOwner
import androidx.preference.Preference
import com.tasomaniac.devwidget.data.Analytics
import javax.inject.Inject

class OtherSettings @Inject constructor(
    fragment: SettingsFragment,
    private val analytics: Analytics,
    private val version: Version
) : Settings(fragment) {

    override fun onCreate(owner: LifecycleOwner) {
        addPreferencesFromResource(R.xml.pref_others)

        findPreference(R.string.pref_key_open_source).onPreferenceClickListener = listener
        findPreference(R.string.pref_key_contact).onPreferenceClickListener = listener
        findPreference(R.string.pref_key_market).onPreferenceClickListener = listener
        findPreference(R.string.pref_key_version).onPreferenceClickListener = listener
        setupVersionPreference()
    }

    private val listener = Preference.OnPreferenceClickListener {
        when {
            it.isKeyEquals(R.string.pref_key_open_source) -> displayLicensesDialogFragment()
            it.isKeyEquals(R.string.pref_key_contact) -> startContactEmailChooser()
        }

        analytics.sendEvent(
            "Preference Clicked",
            "Clicked Item" to it.key
        )
        false
    }

    private fun setupVersionPreference() {
        val summary = StringBuilder(version.versionName)
        if (BuildConfig.DEBUG) {
            summary.append(" (")
                .append(version.versionCode)
                .append(")")
        }
        val preference = findPreference(R.string.pref_key_version)
        preference.summary = summary
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
