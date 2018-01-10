package com.tasomaniac.devdrawer.configure

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.tasomaniac.devdrawer.data.Filter
import com.tasomaniac.devdrawer.data.FilterDao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.WidgetDao
import com.tasomaniac.devdrawer.rx.emptyDebouncer
import com.tasomaniac.devdrawer.rx.testScheduling
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then

@RunWith(Parameterized::class)
class ConfigureUseCaseTest(
    private val expectedPackageMatchers: Collection<String>,
    private val givenPackages: List<String>
) {

  private val widgetDao: WidgetDao = mock {
    on { findWidgetById(APP_WIDGET_ID) } doReturn Maybe.empty()
  }
  private val filterDao: FilterDao = mock()
  private val useCase = ConfigureUseCase(mock(), widgetDao, mock(), filterDao, mock(), APP_WIDGET_ID,
      emptyDebouncer(), testScheduling())

  @Test
  fun `should find expected packageMatchers`() {
    assertEquals(expectedPackageMatchers, useCase.findPossiblePackageMatchersSync(givenPackages))
  }

  @Test
  fun `should emit persisted packageMatchers`() {
    given(filterDao.findFiltersByWidgetId(APP_WIDGET_ID))
        .willReturn(Flowable.just(SOME_PACKAGE_MATCHERS))

    useCase.packageMatchers()
        .test()
        .assertValue(SOME_PACKAGE_MATCHERS)
  }

  @Test
  fun `given NOT available, should insert and update widget`() {
    given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.empty())

    useCase.updateWidgetName(ANY_WIDGET_NAME)

    then(widgetDao).should().insertWidgetSync(Widget(APP_WIDGET_ID))
    then(widgetDao).should().updateWidgetSync(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
  }

  @Test
  fun `given already available, should update widget`() {
    given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

    useCase.updateWidgetName(ANY_WIDGET_NAME)

    then(widgetDao).should().updateWidgetSync(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
  }

  @Test
  fun `given already available, should emit current widget name`() {
    given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

    useCase.currentWidgetName()
        .test()
        .assertValue(ANY_WIDGET_NAME)
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
