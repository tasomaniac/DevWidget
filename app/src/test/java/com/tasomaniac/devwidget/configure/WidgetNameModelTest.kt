package com.tasomaniac.devwidget.configure

import android.appwidget.AppWidgetManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.extensions.emptyDebouncer
import com.tasomaniac.devwidget.extensions.testScheduling
import com.tasomaniac.devwidget.widget.RemoveViewsCreator
import com.tasomaniac.devwidget.widget.WidgetUpdater
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class WidgetNameModelTest {

    private val widgetDao = mock<WidgetDao> {
        on { findWidgetById(APP_WIDGET_ID) } doReturn Maybe.empty()
        on { insertWidget(any()) } doReturn Completable.complete()
        on { updateWidget(any()) } doReturn Completable.complete()
    }
    private val appWidgetManager = mock<AppWidgetManager> {
        on { getAppWidgetOptions(APP_WIDGET_ID) } doReturn mock()
    }

    private val widgetNameModel: WidgetNameModel

    init {
        val removeViewsCreator = mock<RemoveViewsCreator> {
            on { create() } doReturn mock()
        }
        val remoteViewCreatorFactory = mock<RemoveViewsCreator.Factory> {
            on { create(any(), any()) } doReturn removeViewsCreator
        }
        val widgetUpdater = WidgetUpdater(mock(), appWidgetManager, remoteViewCreatorFactory, widgetDao)

        widgetNameModel = WidgetNameModel(
            widgetDao,
            widgetUpdater,
            APP_WIDGET_ID,
            emptyDebouncer(),
            testScheduling()
        )
    }

    @Test
    fun `given NOT available, should insert and update widget`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.empty())

        widgetNameModel.updateWidgetName(ANY_WIDGET_NAME)

        then(widgetDao).should().insertWidget(Widget(APP_WIDGET_ID))
        then(widgetDao).should().updateWidget(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
        then(appWidgetManager).should().updateAppWidget(eq(APP_WIDGET_ID), any())
    }

    @Test
    fun `given already available, should update widget`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

        widgetNameModel.updateWidgetName(ANY_WIDGET_NAME)

        then(widgetDao).should().updateWidget(Widget(APP_WIDGET_ID, ANY_WIDGET_NAME))
        then(appWidgetManager).should().updateAppWidget(eq(APP_WIDGET_ID), any())
    }

    @Test
    fun `given already available, should emit current widget name`() {
        given(widgetDao.findWidgetById(APP_WIDGET_ID)).willReturn(Maybe.just(ANY_WIDGET))

        widgetNameModel.currentWidgetName()
            .test()
            .assertValue(ANY_WIDGET_NAME)
    }

    companion object {

        private const val APP_WIDGET_ID = 1
        private const val ANY_WIDGET_NAME = "any_name"
        private val ANY_WIDGET = Widget(APP_WIDGET_ID, ANY_WIDGET_NAME)
    }
}
