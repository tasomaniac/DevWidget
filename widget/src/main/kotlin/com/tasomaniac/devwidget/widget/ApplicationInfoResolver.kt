package com.tasomaniac.devwidget.widget

import android.content.pm.ApplicationInfo
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

    private val mainUser = userManager.userProfiles[0]

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
            ?: resolveNoLauncherAppForMainUser(user, packageName)
    }

    private fun resolveNoLauncherAppForMainUser(user: UserHandle, packageName: String): DisplayApplicationInfo? {
        if (user == mainUser) {
            try {
                return packageManager.getApplicationInfo(packageName, 0).toDisplayApplicationInfo()
            } catch (ignored: PackageManager.NameNotFoundException) {
            }
        }
        return null
    }

    private fun LauncherActivityInfo.toDisplayApplicationInfo() =
        DisplayApplicationInfo(
            packageManager.getUserBadgedLabel(label, user),
            applicationInfo.packageName,
            getBadgedIcon(0),
            user
        )

    private fun ApplicationInfo.toDisplayApplicationInfo() =
        DisplayApplicationInfo(
            packageManager.getApplicationLabel(this),
            packageName,
            loadIcon(packageManager),
            mainUser
        )
}
