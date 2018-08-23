package com.tasomaniac.devwidget.settings

import androidx.core.app.ShareCompat
import androidx.preference.Preference
import com.tasomaniac.devwidget.BuildConfig
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.data.Analytics
import javax.inject.Inject

class OtherSettings @Inject constructor(
    fragment: SettingsFragment,
    private val analytics: Analytics
) : Settings(fragment) {

    override fun setup() {
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
