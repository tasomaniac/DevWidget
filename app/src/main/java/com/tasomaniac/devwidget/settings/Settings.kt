package com.tasomaniac.devwidget.settings

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.support.annotation.StringRes
import android.support.annotation.XmlRes
import android.support.v7.preference.Preference

abstract class Settings(
    private val fragment: SettingsFragment
) : LifecycleObserver {

    val context get() = fragment.context!!
    val activity get() = fragment.activity!!

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun setup() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun release() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun resume() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun pause() = Unit

    fun addPreferencesFromResource(@XmlRes resId: Int) = fragment.addPreferencesFromResource(resId)

    fun startActivity(intent: Intent) = fragment.startActivity(intent)

    fun findPreference(@StringRes keyResource: Int): Preference = fragment.run {
        findPreference(getString(keyResource))
    }

    fun Preference.isKeyEquals(@StringRes keyRes: Int) = key.isKeyEquals(keyRes)

    fun String.isKeyEquals(@StringRes keyRes: Int) = fragment.getString(keyRes) == this
}
