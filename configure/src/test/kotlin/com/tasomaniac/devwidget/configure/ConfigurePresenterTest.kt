package com.tasomaniac.devwidget.configure

import androidx.lifecycle.ViewModelProvider
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import com.tasomaniac.devwidget.data.FullWidgetDao
import com.tasomaniac.devwidget.navigation.Navigator
import com.tasomaniac.devwidget.navigation.settingsCommand
import com.tasomaniac.devwidget.test.given
import com.tasomaniac.devwidget.test.testScheduling
import com.tasomaniac.devwidget.test.willReturn
import com.uber.autodispose.lifecycle.TestLifecycleScopeProvider
import com.uber.autodispose.lifecycle.TestLifecycleScopeProvider.TestLifecycle.STARTED
import io.reactivex.Flowable
import org.junit.Test

class ConfigurePresenterTest {

    private val packageMatcherModel = mock<PackageMatcherModel> {
        given { findPossiblePackageMatchers() } willReturn Flowable.never()
        given { findAvailablePackageMatchers() } willReturn Flowable.never()
    }
    private val viewModelProvider = mock<ViewModelProvider> {
        given { get(PackageMatcherModel::class.java) } willReturn packageMatcherModel
    }
    private val view = mock<ConfigureView>()
    private val navigator = mock<Navigator>()
    private val fullWidgetDao = mock<FullWidgetDao> {
        given { findWidgetById(100) } willReturn Flowable.never()
    }

    private val presenter = ConfigurePresenter(
        viewModelProvider,
        mock(),
        mock(),
        false,
        navigator,
        fullWidgetDao,
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
