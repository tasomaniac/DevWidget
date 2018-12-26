package com.tasomaniac.devwidget.configure

import androidx.lifecycle.ViewModelProvider
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import com.tasomaniac.devwidget.data.WidgetAppDao
import com.tasomaniac.devwidget.navigation.Navigator
import com.tasomaniac.devwidget.navigation.settingsCommand
import com.tasomaniac.devwidget.test.given
import com.tasomaniac.devwidget.test.testScheduling
import com.tasomaniac.devwidget.test.willReturn
import com.uber.autodispose.lifecycle.TestLifecycleScopeProvider
import com.uber.autodispose.lifecycle.TestLifecycleScopeProvider.TestLifecycle.STARTED
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.Test

class ConfigurePresenterTest {

    private val widgetNameModel = mock<WidgetNameModel> {
        given { currentWidgetName() } willReturn Maybe.never()
    }
    private val packageMatcherModel = mock<PackageMatcherModel> {
        given { findPossiblePackageMatchers() } willReturn Flowable.never()
        given { findAvailablePackageMatchers() } willReturn Flowable.never()
    }
    private val viewModelProvider = mock<ViewModelProvider> {
        given { get(WidgetNameModel::class.java) } willReturn widgetNameModel
        given { get(PackageMatcherModel::class.java) } willReturn packageMatcherModel
    }
    private val view = mock<ConfigureView>()
    private val navigator = mock<Navigator>()
    private val widgetAppDao = mock<WidgetAppDao> {
        given { findWidgetWithPackagesById(100) } willReturn Flowable.never()
    }

    private val presenter = ConfigurePresenter(
        viewModelProvider,
        mock(),
        mock(),
        false,
        navigator,
        widgetAppDao,
        mock(),
        100,
        testScheduling(),
        TestLifecycleScopeProvider.createInitial(STARTED),
        mock()
    )

    init {
        presenter.bind(view)
    }

    @Test
    fun `should navigate to Settings when settings clicked`() {
        captureSettingsClicked().invoke()

        then(navigator).should().navigate(settingsCommand())
    }

    private fun captureSettingsClicked() = with(argumentCaptor<() -> Unit>()) {
        then(view).should().onSettingsClicked = capture()
        firstValue
    }
}
