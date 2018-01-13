package com.tasomaniac.devdrawer.settings

import android.content.SharedPreferences
import android.content.res.Resources
import android.support.annotation.StringRes
import com.tasomaniac.devdrawer.R
import javax.inject.Inject

class SortingPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val resources: Resources) {

  private val key = resources.getString(R.string.pref_key_sorting)

  val sorting: Sorting
    get() {
      val value = sharedPreferences.getString(key, null)
      return Sorting.fromValue(resources, value) ?: Sorting.ORDER_ADDED
    }

  enum class Sorting(
      @StringRes val value: Int,
      @StringRes val entry: Int
  ) {
    ORDER_ADDED(R.string.pref_value_sorting_order_added, R.string.pref_entry_sorting_order_added),
    ALPHABETICALLY(R.string.pref_value_sorting_alphabetically, R.string.pref_entry_sorting_alphabetically);

    fun stringVale(resources: Resources) = resources.getString(value)

    companion object {

      internal fun fromValue(resources: Resources, value: String?) =
          Sorting.values()
              .firstOrNull { it.stringVale(resources) == value }
    }
  }
}
