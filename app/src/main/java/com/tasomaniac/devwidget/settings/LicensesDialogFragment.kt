package com.tasomaniac.devwidget.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.webkit.WebView
import com.tasomaniac.devwidget.R

class LicensesDialogFragment : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        @SuppressLint("InflateParams")
        val view = layoutInflater.inflate(R.layout.dialog_licenses, null)

        val licenses = view.findViewById<WebView>(R.id.licenses)
        licenses.loadUrl("file:///android_asset/open_source_licenses.html")

        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.pref_title_open_source)
            .setView(view)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }

    companion object {

        fun newInstance() = LicensesDialogFragment()
    }

}
