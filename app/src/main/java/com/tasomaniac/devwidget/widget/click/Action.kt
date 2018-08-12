package com.tasomaniac.devwidget.widget.click

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class Action(
    @DrawableRes val icon: Int,
    @StringRes val text: Int,
    val navigate: () -> Unit
)
