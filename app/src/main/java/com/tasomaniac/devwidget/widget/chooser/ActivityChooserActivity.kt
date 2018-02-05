package com.tasomaniac.devwidget.widget.chooser

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.widget.DisplayResolveInfo
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_chooser_list.*
import javax.inject.Inject

class ActivityChooserActivity : DaggerAppCompatActivity() {

    @Inject lateinit var adapter: ResolveListAdapter

    private val extraPackageName
        get() = intent.getStringExtra(EXTRA_PACKAGE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser_list)

        resolverDrawerLayout.setOnDismissedListener(::finish)

        val apps = packageManager
            .getPackageInfo(extraPackageName, PackageManager.GET_ACTIVITIES)
            .activities
            .map { DisplayResolveInfo(packageManager, it) }
        adapter.setApplications(apps)
        adapter.displayExtendedInfo = true
        activityChooserList.adapter = adapter
    }

    companion object {

        private const val EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME"

        fun createIntent(context: Context, packageName: String) =
            Intent(context, ActivityChooserActivity::class.java).apply {
                putExtra(EXTRA_PACKAGE_NAME, packageName)
            }
    }
}
