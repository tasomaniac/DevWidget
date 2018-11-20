package com.tasomaniac.devwidget.data

import android.app.Activity
import timber.log.Timber
import javax.inject.Inject

interface Analytics {

    fun sendScreenView(activity: Activity, screenName: String)

    fun sendValueEvent(name: String, value: String) =
        sendEvent(name, "value" to value)

    fun sendEvent(name: String, vararg params: Pair<String, String>)

    class DebugAnalytics @Inject constructor() : Analytics {

        override fun sendScreenView(activity: Activity, screenName: String) {
            Timber.tag("Analytics").d("Screen: %s", screenName)
        }

        override fun sendEvent(name: String, vararg params: Pair<String, String>) {
            Timber.tag("Analytics").d(
                """Event recorded:
                    |    Name: $name
                    |    Parameters (key, value): $params
                    |""".trimMargin()
            )
        }
    }
}
