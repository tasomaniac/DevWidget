package com.tasomaniac.devwidget.data.updater

import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import javax.inject.Inject

class PackageResolver @Inject constructor(private val packageManager: PackageManager) {

    fun allApplications(): List<String> {
        val flag = if (SDK_INT >= N) PackageManager.MATCH_UNINSTALLED_PACKAGES else 0
        return packageManager.getInstalledApplications(flag)
            .map { it.packageName }
            .distinct()
    }
}
