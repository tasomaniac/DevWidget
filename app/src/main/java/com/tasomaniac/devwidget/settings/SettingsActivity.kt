package com.tasomaniac.devwidget.settings

import android.app.backup.BackupManager
import android.content.SharedPreferences
import android.os.Bundle
import com.tasomaniac.devwidget.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.settings_activity.*
import javax.inject.Inject

class SettingsActivity : DaggerAppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

  @Inject lateinit var sharedPreferences: SharedPreferences

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.settings_activity)
    setSupportActionBar(toolbar)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .add(R.id.fragment_container, SettingsFragment.newInstance())
          .commit()
    }
  }

  override fun onResume() {
    super.onResume()
    sharedPreferences.registerOnSharedPreferenceChangeListener(this)
  }

  override fun onPause() {
    sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    super.onPause()
  }

  override fun onSupportNavigateUp(): Boolean {
    finish()
    return true
  }

  override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
    BackupManager(this).dataChanged()
  }
}
