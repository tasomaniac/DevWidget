package com.tasomaniac.devwidget.widget

import android.content.pm.PackageManager
import timber.log.Timber
import javax.inject.Inject

class ApplicationInfoResolver @Inject constructor(private val packageManager: PackageManager) {

    fun resolve(packageName: String): DisplayApplicationInfo? = try {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        DisplayApplicationInfo(
            appInfo.loadLabel(packageManager),
            appInfo.packageName,
            appInfo.loadIcon(packageManager)
        )
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e)
        null
    }
}
