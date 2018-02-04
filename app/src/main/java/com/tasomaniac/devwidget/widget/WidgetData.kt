package com.tasomaniac.devwidget.widget

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

sealed class DisplayInfo(
    open val label: CharSequence,
    open val secondaryLabel: CharSequence?,
    open val packageName: String,
    open val icon: Drawable
)

data class DisplayApplicationInfo(
    override val label: CharSequence,
    override val packageName: String,
    override val icon: Drawable
) : DisplayInfo(label, null, packageName, icon) {

    constructor(packageManager: PackageManager, appInfo: ApplicationInfo) : this(
        appInfo.loadLabel(packageManager),
        appInfo.packageName,
        appInfo.loadIcon(packageManager)
    )
}

data class DisplayResolveInfo(
    override val label: CharSequence,
    override val packageName: String,
    override val icon: Drawable
) : DisplayInfo(label, null, packageName, icon) {

    constructor(packageManager: PackageManager, resolveInfo: ResolveInfo) : this(
        resolveInfo.loadLabel(packageManager),
        resolveInfo.activityInfo.applicationInfo.packageName,
        resolveInfo.loadIcon(packageManager)
    )
}
