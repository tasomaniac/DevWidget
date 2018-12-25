package com.tasomaniac.devwidget.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri

data class UriCommand(val uri: Uri) : IntentCommand {
    override fun createIntent(context: Context) = Intent(Intent.ACTION_VIEW, uri)
}
