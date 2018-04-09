package com.tasomaniac.devwidget.widget.chooser

import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.LauncherApps
import android.os.Bundle
import android.os.UserManager
import android.support.annotation.StringRes
import android.widget.Toast
import com.tasomaniac.devwidget.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_chooser_list.activityChooserList
import kotlinx.android.synthetic.main.activity_chooser_list.resolverDrawerLayout
import javax.inject.Inject



class ActivityChooserActivity : DaggerAppCompatActivity() {

    @Inject lateinit var adapter: ResolveListAdapter

    private val extraPackageName
        get() = intent.getStringExtra(EXTRA_PACKAGE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser_list)
        resolverDrawerLayout.setOnDismissedListener(::finish)

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val userManager = getSystemService(Context.USER_SERVICE) as UserManager
        val launcherApps = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

        val apps = userManager.userProfiles
            .flatMap {
                launcherApps.getActivityList(extraPackageName, it)
                    .map {
                        DisplayResolveInfo(
                            it.componentName,
                            it.label,
                            it.getBadgedIcon(activityManager.launcherLargeIconDensity)
                        )
                    }
            }

        if (apps.size == 1) {
            launch(apps.first())
            finish()
            return
        }

//        val apps = packageManager
//            .getPackageInfo(extraPackageName, PackageManager.GET_ACTIVITIES)
//            .activities
//            .filter { it.exported }
//            .map(::toDisplayResolveInfo)
        adapter.submitList(apps)
        adapter.itemClickListener = {
            launch(it)
        }
        activityChooserList.adapter = adapter
    }

    private fun launch(it: DisplayResolveInfo) {
        Intent().apply {
            component = it.component
            addCategory(Intent.CATEGORY_LAUNCHER)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        }.start()
    }

    private fun toDisplayResolveInfo(activityInfo: ActivityInfo) =
        DisplayResolveInfo(
            activityInfo.componentName(),
            activityInfo.loadLabel(packageManager),
            activityInfo.loadIcon(packageManager)
        )

    fun Intent.start() =
        try {
            startActivity(this)
        } catch (e: ActivityNotFoundException) {
            toast(R.string.widget_error_activity_cannot_be_launched)
        }

    private fun toast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME"

        fun createIntent(context: Context, packageName: String) =
            Intent(context, ActivityChooserActivity::class.java).apply {
                putExtra(EXTRA_PACKAGE_NAME, packageName)
            }
    }
}
