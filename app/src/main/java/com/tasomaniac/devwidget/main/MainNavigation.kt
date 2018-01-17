package com.tasomaniac.devwidget.main

import android.content.Context
import android.os.Build.VERSION_CODES.O
import android.support.annotation.RequiresApi
import com.tasomaniac.devwidget.configure.ConfigureActivity
import javax.inject.Inject

class MainNavigation @Inject constructor() {

    @RequiresApi(O)
    fun navigateForPinning(context: Context) {
        val intent = ConfigureActivity.createIntentForPinning(context)
        context.startActivity(intent)
    }
}
