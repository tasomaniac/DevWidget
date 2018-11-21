package com.tasomaniac.devwidget.app

import com.tasomaniac.devwidget.settings.Version
import javax.inject.Inject

class AppVersion @Inject constructor() : Version {
    override val versionName: String
        get() = BuildConfig.VERSION_NAME
    override val versionCode: Int
        get() = BuildConfig.VERSION_CODE
}
