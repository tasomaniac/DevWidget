package com.tasomaniac.devwidget.settings

import android.content.res.Resources
import android.support.annotation.StringRes

interface PreferenceEntries {
    @get:StringRes
    val value: Int
    @get:StringRes
    val entry: Int

    fun stringVale(resources: Resources) = resources.getString(value)

    companion object {

        inline fun <reified T> fromValue(resources: Resources, value: String?): T?
                where T : Enum<T>, T : PreferenceEntries {
            return enumValues<T>()
                .firstOrNull { it.stringVale(resources) == value }
        }
    }
}
