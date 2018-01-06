package com.tasomaniac.devdrawer.configure

import android.content.pm.PackageManager
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.Filter
import com.tasomaniac.devdrawer.data.FilterDao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.testScheduling
import com.tasomaniac.devdrawer.widget.WidgetUpdater
import io.reactivex.Maybe
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class ConfigureUseCaseTest(
    private val expectedPackageMatchers: Collection<String>,
    private val givenPackages: List<String>
) {

  private val dao: Dao = mock(Dao::class.java).apply {
    given(findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.empty())
  }
  private val filterDao: FilterDao = mock(FilterDao::class.java)
  private val useCase = ConfigureUseCase(
      mock(PackageManager::class.java),
      dao,
      filterDao,
      mock(WidgetUpdater::class.java),
      APP_WIDGET_ID,
      testScheduling()
  )

  @Test
  fun `should find expected packageMatchers`() {
    assertEquals(expectedPackageMatchers, useCase.findPossiblePackageMatchersSync(givenPackages))
  }

  @Test
  fun `given NOT available, should insert and update widget`() {
    given(dao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.empty())

    useCase.updateWidgetName(ANY_WIDGET_NAME)

    then(dao).should().insertWidgetSync(Widget(APP_WIDGET_ID))
    then(dao).should().updateWidgetSync(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
  }

  @Test
  fun `given already available, should update widget`() {
    given(dao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

    useCase.updateWidgetName(ANY_WIDGET_NAME)

    then(dao).should().updateWidgetSync(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
  }

  @Test
  fun `should insert packageMatchers`() {
    useCase.insertPackageMatcher("com.tasomaniac.*")
        .test()

    val expected = listOf(Filter("com.tasomaniac.*", APP_WIDGET_ID))
    then(filterDao).should().insertFilterSync(expected)
  }

  companion object {

    @JvmStatic
    @Parameters(name = "expectedPackageMatchers: {0} given: {1}")
    fun data() = arrayOf(
        arrayOf(
            setOf("com.*", "com.tasomaniac.*", "com.tasomaniac.devdrawer"),
            listOf("com.tasomaniac.devdrawer")),
        arrayOf(
            setOf(
                "com.*", "com.tasomaniac.*", "com.tasomaniac.devdrawer",
                "de.*", "de.is24.*", "de.is24.android"
            ),
            listOf("com.tasomaniac.devdrawer", "de.is24.android")),
        arrayOf(
            setOf(
                "com.*", "com.tasomaniac.*",
                "com.tasomaniac.devdrawer",
                "com.tasomaniac.openwith"
            ),
            listOf("com.tasomaniac.devdrawer", "com.tasomaniac.openwith")),
        arrayOf(
            setOf("somePackage.*", "somePackage"),
            listOf("somePackage"))
    )

    private val SOME_PACKAGE_MATCHERS = listOf("com.*")
    private const val APP_WIDGET_ID = 1
    private const val ANY_WIDGET_NAME = "any_name"
    private val ANY_WIDGET = Widget(APP_WIDGET_ID, ANY_WIDGET_NAME)
  }
}
