package com.tasomaniac.devdrawer.configure

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class ConfigureUseCaseTest(
    private val expected: Collection<String>,
    private val givenPackages: Collection<String>
) {

  private val packageManager: PackageManager = mock(PackageManager::class.java)
  private val useCase = ConfigureUseCase(packageManager)

  @Test
  fun findExistingPackages() {
    given(packageManager.queryIntentActivities(any(), eq(0)))
        .willReturn(givenPackages.map(::toResolveInfo))

    assertEquals(expected, useCase.findExistingPackagesSync(Intent()))
  }

  companion object {

    @JvmStatic
    @Parameters(name = "expected: {0} given: {1}")
    fun data() = arrayOf(
        arrayOf(
            setOf("com.*", "com.tasomaniac.*", "com.tasomaniac.devdrawer"),
            setOf("com.tasomaniac.devdrawer")),
        arrayOf(
            setOf(
                "com.*", "com.tasomaniac.*", "com.tasomaniac.devdrawer",
                "de.*", "de.is24.*", "de.is24.android"
            ),
            setOf("com.tasomaniac.devdrawer", "de.is24.android")),
        arrayOf(
            setOf(
                "com.*", "com.tasomaniac.*",
                "com.tasomaniac.devdrawer",
                "com.tasomaniac.openwith"
            ),
            setOf("com.tasomaniac.devdrawer", "com.tasomaniac.openwith")),
        arrayOf(
            setOf("somePackage.*", "somePackage"),
            setOf("somePackage"))
    )

    fun toResolveInfo(givenPackage: String) =
        ResolveInfo().apply {
          activityInfo = ActivityInfo().apply {
            applicationInfo = ApplicationInfo().apply {
              packageName = givenPackage
            }
          }
        }
  }
}
