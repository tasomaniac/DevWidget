package com.tasomaniac.devwidget.configure

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.tasomaniac.devwidget.data.Filter
import com.tasomaniac.devwidget.data.FilterDao
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.rx.emptyDebouncer
import com.tasomaniac.devwidget.rx.testScheduling
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then

@RunWith(Parameterized::class)
class ConfigureUseCaseTest(
    private val expectedPackageMatchers: List<String>,
    private val givenPackages: List<String>
) {

  private val packageResolver = mock<PackageResolver> {
    on { allLauncherPackages() } doReturn givenPackages
  }
  private val widgetDao = mock<WidgetDao> {
    on { findWidgetById(APP_WIDGET_ID) } doReturn Maybe.empty()
  }
  private val filterDao = mock<FilterDao> {
    on { findFiltersByWidgetId(APP_WIDGET_ID) } doReturn Flowable.just(emptyList())
  }

  private val useCase = ConfigureUseCase(packageResolver, widgetDao, mock(), filterDao, mock(),
      APP_WIDGET_ID,
      emptyDebouncer(), testScheduling())

  @Test
  fun `should find expected packageMatchers`() {
    useCase.findPossiblePackageMatchers()
        .test()
        .assertValue(expectedPackageMatchers)
  }

  @Test
  fun `should find possible packageMatchers with persisted filtered out`() {
    val persisted = listOf("com.*")
    given(filterDao.findFiltersByWidgetId(APP_WIDGET_ID))
        .willReturn(Flowable.just(persisted))

    val expected = expectedPackageMatchers - persisted

    useCase.findPossiblePackageMatchers()
        .test()
        .assertValue(expected)
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
    then(widgetDao).should().updateWidgetSync(Widget(APP_WIDGET_ID,
        ANY_WIDGET_NAME))
  }

  @Test
  fun `given already available, should update widget`() {
    given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(
        ANY_WIDGET))

    useCase.updateWidgetName(ANY_WIDGET_NAME)

    then(widgetDao).should().updateWidgetSync(Widget(APP_WIDGET_ID,
        ANY_WIDGET_NAME))
  }

  @Test
  fun `given already available, should emit current widget name`() {
    given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(
        ANY_WIDGET))

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
            listOf("com.*", "com.tasomaniac.*", "com.tasomaniac.devwidget"),
            listOf("com.tasomaniac.devwidget")),
        arrayOf(
            listOf(
                "com.*", "com.tasomaniac.*", "com.tasomaniac.devwidget",
                "de.*", "de.is24.*", "de.is24.android"
            ),
            listOf("com.tasomaniac.devwidget", "de.is24.android")),
        arrayOf(
            listOf(
                "com.*", "com.tasomaniac.*",
                "com.tasomaniac.devwidget",
                "com.tasomaniac.openwith"
            ),
            listOf("com.tasomaniac.devwidget", "com.tasomaniac.openwith")),
        arrayOf(
            listOf("somePackage.*", "somePackage"),
            listOf("somePackage"))
    )

    private val SOME_PACKAGE_MATCHERS = listOf("com.*")
    private const val APP_WIDGET_ID = 1
    private const val ANY_WIDGET_NAME = "any_name"
    private val ANY_WIDGET = Widget(APP_WIDGET_ID,
        ANY_WIDGET_NAME)
  }
}
