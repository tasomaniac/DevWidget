package com.tasomaniac.devwidget.widget.chooser

import android.content.ComponentName
import android.content.pm.ActivityInfo

fun ActivityInfo.componentName() = ComponentName(applicationInfo.packageName, name)
