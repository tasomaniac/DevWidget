package com.tasomaniac.devwidget.widget.click

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

class Action(
    @DrawableRes val icon: Int,
    @StringRes val text: Int,
    val navigate: () -> Unit
)
