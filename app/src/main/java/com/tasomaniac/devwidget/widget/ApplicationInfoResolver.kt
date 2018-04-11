package com.tasomaniac.devwidget.widget

import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.UserHandle
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
                resolveFor(user, packageName)
            }
    }

    private fun resolveFor(user: UserHandle, packageName: String): DisplayApplicationInfo? {
        return launcherApps
            .getActivityList(packageName, user)
            .firstOrNull()
            ?.toDisplayApplicationInfo()
    }

    private fun LauncherActivityInfo.toDisplayApplicationInfo() =
        DisplayApplicationInfo(
            packageManager.getUserBadgedLabel(label, user),
            applicationInfo.packageName,
            getBadgedIcon(0),
            user
        )
}
