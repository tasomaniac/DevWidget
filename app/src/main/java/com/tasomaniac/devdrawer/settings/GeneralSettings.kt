package com.tasomaniac.devdrawer.settings

import android.appwidget.AppWidgetManager
import android.content.SharedPreferences
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Analytics
import com.tasomaniac.devdrawer.data.WidgetDao
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import io.reactivex.Completable
import javax.inject.Inject

class GeneralSettings @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val sortingPreferences: SortingPreferences,
    private val appWidgetManager: AppWidgetManager,
    private val scheduling: SchedulingStrategy,
    private val widgetDao: WidgetDao,
    private val analytics: Analytics,
    fragment: SettingsFragment
) : Settings(fragment),
    SharedPreferences.OnSharedPreferenceChangeListener {

  override fun setup() {
    addPreferencesFromResource(R.xml.pref_general)
    sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    updateSummary(sortingPreferences.sorting)
  }

  override fun release() {
    sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
  }

  override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
    if (key.isKeyEquals(R.string.pref_key_sorting)) {
      val sorting = sortingPreferences.sorting
      updateSummary(sorting)

      updateAllWidgets()
          .compose(scheduling.forCompletable())
          .subscribe()

      analytics.sendEvent("Preference", "Sorting", sorting.stringVale(context.resources))
    }
  }

  private fun updateSummary(sorting: SortingPreferences.Sorting) {
    findPreference(R.string.pref_key_sorting).setSummary(sorting.entry)
  }

  private fun updateAllWidgets(): Completable {
    return widgetDao.allWidgetIds()
        .map { it.toIntArray() }
        .flatMapCompletable {
          Completable.fromAction {
            appWidgetManager.notifyAppWidgetViewDataChanged(it, R.id.widgetAppList)
          }
        }
  }
}
