package com.tasomaniac.devwidget.configure

import android.appwidget.AppWidgetManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.then
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.test.given
import com.tasomaniac.devwidget.test.testScheduling
import com.tasomaniac.devwidget.test.willReturn
import com.tasomaniac.devwidget.widget.WidgetUpdater
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class WidgetNameModelTest {

    private val widgetDao = mock<WidgetDao> {
        given { findWidgetById(APP_WIDGET_ID) } willReturn Maybe.empty()
        given { insertWidget(any()) } willReturn Completable.complete()
        given { updateWidget(any()) } willReturn Completable.complete()
    }
    private val appWidgetManager = mock<AppWidgetManager> {
        given { getAppWidgetOptions(APP_WIDGET_ID) } willReturn mock()
    }
    private val widgetUpdater = mock<WidgetUpdater> {
        given { appWidgetManager } willReturn appWidgetManager
        given { update(any(), any()) } willReturn Completable.complete()
        given { update(any(), any(), any()) } willReturn Completable.complete()
        given { updateAll() } willReturn Completable.complete()
    }

    @Test
    fun `given NOT available, should insert and update widget when constructed`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.empty())

        testViewModel()

        then(widgetDao).should().insertWidget(Widget(APP_WIDGET_ID))
        then(widgetUpdater).should().updateAll()
    }

    @Test
    fun `given already available, do not insert but update all widgets`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

        testViewModel()

        then(widgetDao).should(never()).insertWidget(any())
        then(widgetUpdater).should().updateAll()
    }

    @Test
    fun `given NOT available, should insert and update widget`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.empty())

        testViewModel().updateWidgetName(ANY_WIDGET_NAME)

        then(widgetDao).should().insertWidget(Widget(APP_WIDGET_ID))
        then(widgetDao).should().updateWidget(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
        then(widgetUpdater).should().update(APP_WIDGET_ID, ANY_WIDGET_NAME)
    }

    @Test
    fun `given already available, should update widget`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

        testViewModel().updateWidgetName(ANY_WIDGET_NAME)

        then(widgetDao).should(never()).insertWidget(any())
        then(widgetDao).should().updateWidget(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
        then(widgetUpdater).should().update(APP_WIDGET_ID, ANY_WIDGET_NAME)
    }

    private fun testViewModel() = WidgetNameModel(
        widgetDao,
        widgetUpdater,
        APP_WIDGET_ID,
        testScheduling()
    )

    companion object {

        private const val APP_WIDGET_ID = 1
        private const val ANY_WIDGET_NAME = "any_name"
        private val ANY_WIDGET = Widget(APP_WIDGET_ID, ANY_WIDGET_NAME)
    }
}
