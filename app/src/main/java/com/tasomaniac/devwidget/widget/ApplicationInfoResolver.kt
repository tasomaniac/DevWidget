package com.tasomaniac.devwidget.widget

import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.UserManager
import javax.inject.Inject

class ApplicationInfoResolver @Inject constructor(
    private val packageManager: PackageManager,
    private val userManager: UserManager,
    private val launcherApps: LauncherApps
) {

    fun resolve(packageName: String): List<DisplayApplicationInfo> {
        return userManager.userProfiles
            .mapNotNull { user ->
                launcherApps
                    .getActivityList(packageName, user)
                    .firstOrNull()
                    ?.let {
                        DisplayApplicationInfo(
                            packageManager.getUserBadgedLabel(it.label, user),
                            it.applicationInfo.packageName,
                            it.getBadgedIcon(0)
                        )
                    }
            }
    }
}
