package com.tasomaniac.devwidget.widget.click

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tasomaniac.devwidget.navigation.Command

class Action(
    @StringRes val text: Int,
    @DrawableRes val icon: Int? = null,
    val command: (packageName: String) -> Command
)
