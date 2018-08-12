package com.tasomaniac.devwidget.configure

import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.widget.RemoteViews
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.rx.emptyDebouncer
import com.tasomaniac.devwidget.rx.testScheduling
import com.tasomaniac.devwidget.widget.RemoveViewsCreator
import com.tasomaniac.devwidget.widget.WidgetUpdater
import io.reactivex.Maybe
import org.junit.Test

class WidgetNameModelTest {

    private val widgetDao = mock<WidgetDao> {
        on { findWidgetById(APP_WIDGET_ID) } doReturn Maybe.empty()
    }
    private val appWidgetManager = mock<AppWidgetManager> {
        on { getAppWidgetOptions(APP_WIDGET_ID) } doReturn mock<Bundle>()
    }
    private val removeViewsCreator = mock<RemoveViewsCreator> {
        on { create(any(), any()) } doReturn mock<RemoteViews>()
    }
    private val widgetUpdater = WidgetUpdater(mock(), appWidgetManager, removeViewsCreator, widgetDao)

    private val useCase = WidgetNameModel(
        widgetDao,
        widgetUpdater,
        APP_WIDGET_ID,
        emptyDebouncer(),
        testScheduling()
    )

    @Test
    fun `given NOT available, should insert and update widget`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.empty())

        useCase.updateWidgetName(ANY_WIDGET_NAME)

        then(widgetDao).should().insertWidgetSync(Widget(APP_WIDGET_ID))
        then(widgetDao).should().updateWidgetSync(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
        then(appWidgetManager).should().updateAppWidget(eq(APP_WIDGET_ID), any())
    }

    @Test
    fun `given already available, should update widget`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

        useCase.updateWidgetName(ANY_WIDGET_NAME)

        then(widgetDao).should().updateWidgetSync(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
        then(appWidgetManager).should().updateAppWidget(eq(APP_WIDGET_ID), any())
    }

    @Test
    fun `given already available, should emit current widget name`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

        useCase.currentWidgetName()
            .test()
            .assertValue(ANY_WIDGET_NAME)
    }

    companion object {

        private const val APP_WIDGET_ID = 1
        private const val ANY_WIDGET_NAME = "any_name"
        private val ANY_WIDGET = Widget(APP_WIDGET_ID, ANY_WIDGET_NAME)
    }
}
