package com.tasomaniac.devwidget.main

import android.os.Build.VERSION_CODES.O
import androidx.annotation.RequiresApi
import com.tasomaniac.devwidget.configure.WidgetPinRequestCommand
import com.tasomaniac.devwidget.navigation.Navigator
import javax.inject.Inject

internal class MainNavigation @Inject constructor(private val navigator: Navigator) {

    @RequiresApi(O)
    fun navigateForPinning() {
        navigator.navigate(WidgetPinRequestCommand)
    }
}
