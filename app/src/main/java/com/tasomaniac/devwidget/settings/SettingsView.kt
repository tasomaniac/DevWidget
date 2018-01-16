package com.tasomaniac.devwidget.settings

interface SettingsView {

  fun setup() = Unit
  fun release() = Unit
  fun resume() = Unit
  fun pause() = Unit
}
