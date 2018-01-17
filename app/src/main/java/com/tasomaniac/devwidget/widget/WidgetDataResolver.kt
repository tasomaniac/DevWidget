package com.tasomaniac.devwidget.widget

import android.content.pm.PackageManager
import timber.log.Timber
import javax.inject.Inject

class WidgetDataResolver @Inject constructor(private val packageManager: PackageManager) {

    fun resolve(packageName: String): WidgetData? = try {
        val appInfo = packageManager
            .getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            .applicationInfo

        WidgetData(
            label = appInfo.loadLabel(packageManager).toString(),
            packageName = appInfo.packageName,
            icon = appInfo.loadIcon(packageManager).toBitmap()
        )
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e)
        null
    }
}
