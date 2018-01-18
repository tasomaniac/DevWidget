package com.tasomaniac.devwidget.configure

import com.nhaarman.mockito_kotlin.*
import com.tasomaniac.devwidget.data.Filter
import com.tasomaniac.devwidget.data.FilterDao
import io.reactivex.Flowable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.BDDMockito

@RunWith(Parameterized::class)
class PackageMatcherModelTest(
    private val expectedPackageMatchers: List<String>,
    private val givenPackages: List<String>
) {

    private val packageResolver = mock<PackageResolver> {
        on { allLauncherPackages() } doReturn givenPackages
    }
    private val filterDao = mock<FilterDao> {
        on { findFiltersByWidgetId(APP_WIDGET_ID) } doReturn Flowable.just(
            emptyList()
        )
    }

    private val model = PackageMatcherModel(packageResolver, mock(), filterDao, APP_WIDGET_ID)

    @Test
    fun `should find expected packageMatchers`() {
        model.findPossiblePackageMatchers()
            .test()
            .assertValue(expectedPackageMatchers)
    }

    @Test
    fun `should find possible packageMatchers with persisted filtered out`() {
        val persisted = listOf("com.*")
        BDDMockito.given(filterDao.findFiltersByWidgetId(APP_WIDGET_ID))
            .willReturn(Flowable.just(persisted))

        val expected = expectedPackageMatchers - persisted

        model.findPossiblePackageMatchers()
            .test()
            .assertValue(expected)
    }

    @Test
    fun `should emit persisted packageMatchers`() {
        BDDMockito.given(filterDao.findFiltersByWidgetId(APP_WIDGET_ID))
            .willReturn(Flowable.just(SOME_PACKAGE_MATCHERS))

        model.packageMatchers()
            .test()
            .assertValue(SOME_PACKAGE_MATCHERS)
    }

    @Test
    fun `should insert packageMatchers`() {
        model.insertPackageMatcher("com.tasomaniac.*")
            .test()

        val expected = listOf(Filter("com.tasomaniac.*", APP_WIDGET_ID))
        then(filterDao).should().insertFilterSync(expected)
    }

    companion object {

        @JvmStatic
        @Parameters(name = "expectedPackageMatchers: {0} given: {1}")
        fun data() = arrayOf(
            arrayOf(
                listOf("com.*", "com.tasomaniac.*", "com.tasomaniac.devwidget"),
                listOf("com.tasomaniac.devwidget")
            ),
            arrayOf(
                listOf(
                    "com.*", "com.tasomaniac.*", "com.tasomaniac.devwidget",
                    "de.*", "de.is24.*", "de.is24.android"
                ),
                listOf("com.tasomaniac.devwidget", "de.is24.android")
            ),
            arrayOf(
                listOf(
                    "com.*", "com.tasomaniac.*",
                    "com.tasomaniac.devwidget",
                    "com.tasomaniac.openwith"
                ),
                listOf("com.tasomaniac.devwidget", "com.tasomaniac.openwith")
            ),
            arrayOf(
                listOf("somePackage.*", "somePackage"),
                listOf("somePackage")
            )
        )

        private val SOME_PACKAGE_MATCHERS = listOf("com.*")
        private const val APP_WIDGET_ID = 1
    }
}
