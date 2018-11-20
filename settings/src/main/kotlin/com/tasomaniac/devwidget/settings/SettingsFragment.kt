package com.tasomaniac.devwidget.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject lateinit var settings: @JvmSuppressWildcards Set<Settings>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        settings.forEach {
            lifecycle.addObserver(it)
        }
    }

    override fun onDestroy() {
        settings.forEach {
            lifecycle.removeObserver(it)
        }
        super.onDestroy()
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
