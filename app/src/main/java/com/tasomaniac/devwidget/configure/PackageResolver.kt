package com.tasomaniac.devwidget.configure

import android.content.Intent
import android.content.pm.PackageManager
import javax.inject.Inject

class PackageResolver @Inject constructor(private val packageManager: PackageManager) {

  fun allLauncherPackages(): List<String> {
    val intent = Intent(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_LAUNCHER)
    return packageManager.queryIntentActivities(intent, 0)
        .map { it.activityInfo.applicationInfo.packageName }
        .toSortedSet()
        .toList()
  }
}
