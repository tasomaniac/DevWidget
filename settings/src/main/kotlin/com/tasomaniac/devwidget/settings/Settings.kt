package com.tasomaniac.devwidget.settings

import androidx.annotation.StringRes
import androidx.annotation.XmlRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.preference.Preference

abstract class Settings(
    private val fragment: SettingsFragment
) : DefaultLifecycleObserver {

    val context get() = fragment.context!!
    val activity get() = fragment.activity!!

    fun addPreferencesFromResource(@XmlRes resId: Int) = fragment.addPreferencesFromResource(resId)

    fun findPreference(@StringRes keyResource: Int): Preference = fragment.run {
        findPreference(getString(keyResource))
    }

    fun Preference.isKeyEquals(@StringRes keyRes: Int) = key.isKeyEquals(keyRes)

    fun String.isKeyEquals(@StringRes keyRes: Int) = fragment.getString(keyRes) == this
}
