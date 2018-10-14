package com.tasomaniac.devwidget.data.updater

import android.content.pm.PackageManager
import javax.inject.Inject

class PackageResolver @Inject constructor(private val packageManager: PackageManager) {

    fun allApplications(): List<String> {
        return packageManager.getInstalledApplications(0)
            .map { it.packageName }
            .distinct()
    }
}
