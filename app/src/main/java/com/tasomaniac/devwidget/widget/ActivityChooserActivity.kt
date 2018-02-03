package com.tasomaniac.devwidget.widget

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tasomaniac.devwidget.R
import kotlinx.android.synthetic.main.activity_chooser_list.*

class ActivityChooserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser_list)

        resolverDrawerLayout.setOnDismissedListener(::finish)
    }
}
