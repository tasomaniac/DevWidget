package com.tasomaniac.devdrawer.widget

import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class PackageMatcherTest(
    private val expectedAppsMatched: List<String>,
    private val givenPackageMatcher: String,
    private val givenPackages: List<String>
) {

  @Test
  fun `should match packages`() {
    val actual = Observable.fromIterable(givenPackages)
        .filter(matchPackage(givenPackageMatcher))
        .test()
        .values()

    assertEquals(expectedAppsMatched, actual)
  }

  companion object {

    @JvmStatic
    @Parameters(name = "expectedPackageMatchers: {0} given: {1}")
    fun data() = arrayOf(
        arrayOf(
            listOf("com.tasomaniac.devdrawer"),
            "com.*",
            listOf("com.tasomaniac.devdrawer")),
        arrayOf(
            listOf("com.tasomaniac.devdrawer"),
            "com.tasomaniac.*",
            listOf("com.tasomaniac.devdrawer", "de.is24.android")),
        arrayOf(
            listOf("com.tasomaniac.devdrawer", "com.tasomaniac.openwith"),
            "com.tasomaniac.*",
            listOf("com.tasomaniac.devdrawer", "com.tasomaniac.openwith")),
        arrayOf(
            emptyList<Any>(),
            "com.*",
            listOf("somePackage"))
    )
  }
}
