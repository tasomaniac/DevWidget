package com.tasomaniac.devwidget.widget

import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.tasomaniac.devwidget.widget.chooser.componentName

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
    override val secondaryLabel: CharSequence,
    override val packageName: String,
    override val icon: Drawable
) : DisplayInfo(label, secondaryLabel, packageName, icon) {

    constructor(packageManager: PackageManager, activityInfo: ActivityInfo) : this(
        activityInfo.loadLabel(packageManager),
        activityInfo.componentName().shortClassName,
        activityInfo.applicationInfo.packageName,
        activityInfo.loadIcon(packageManager)
    )
}
