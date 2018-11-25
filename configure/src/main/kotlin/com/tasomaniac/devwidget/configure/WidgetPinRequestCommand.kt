package com.tasomaniac.devwidget.configure

import android.content.Context
import android.os.Build.VERSION_CODES.O
import androidx.annotation.RequiresApi
import com.tasomaniac.devwidget.navigation.IntentCommand

@RequiresApi(O)
object WidgetPinRequestCommand : IntentCommand {

    override fun createIntent(context: Context) = ConfigureActivity.createIntentForPinning(context)
}
