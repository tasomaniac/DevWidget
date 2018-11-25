package com.tasomaniac.devwidget.configure

import android.annotation.TargetApi
import android.os.Build.VERSION_CODES.O
import com.tasomaniac.devwidget.ViewModelProvider
import com.tasomaniac.devwidget.data.Analytics
import com.tasomaniac.devwidget.extensions.SchedulingStrategy
import com.tasomaniac.devwidget.widget.WidgetUpdater
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.Completable
import javax.inject.Inject

internal class ConfigurePresenter @Inject constructor(
    viewModelProvider: ViewModelProvider,
    widgetUpdater: WidgetUpdater,
    widgetPinner: WidgetPinner,
    configurePinning: ConfigurePinning,
    private val appWidgetId: Int,
    private val scheduling: SchedulingStrategy,
    private val scopeProvider: AndroidLifecycleScopeProvider,
    private val analytics: Analytics
) {

    @TargetApi(O)
    private val updateWidget = Completable.fromAction {
        if (configurePinning) {
            widgetPinner.requestPin()
        } else {
            widgetUpdater.notifyWidgetDataChanged(appWidgetId)
        }
    }

    private val widgetNameModel = viewModelProvider.get<WidgetNameModel>()

    private val packageMatcherModel = viewModelProvider.get<PackageMatcherModel>()

    fun bind(view: ConfigureView) {
        widgetNameModel.currentWidgetName()
            .compose(scheduling.forMaybe())
            .autoDisposable(scopeProvider)
            .subscribe(view::setWidgetName)

        view.widgetNameChanged = widgetNameModel::updateWidgetName

        packageMatcherModel.findPossiblePackageMatchers()
            .compose(scheduling.forFlowable())
            .autoDisposable(scopeProvider)
            .subscribe(view::setItems)

        packageMatcherModel.findAvailablePackageMatchers()
            .compose(scheduling.forFlowable())
            .autoDisposable(scopeProvider)
            .subscribe(view::setFilters)

        view.onConfirmClicked = {
            packageMatcherModel.findAndInsertMatchingApps()
                .andThen(updateWidget)
                .compose(scheduling.forCompletable())
                .doOnComplete {
                    trackConfirm()
                }
                .subscribe {
                    view.finishWith(appWidgetId)
                }
        }

        view.onPackageMatcherAdded = {
            packageMatcherModel.insertPackageMatcher(it)
                .compose(scheduling.forCompletable())
                .subscribe()
        }
    }

    private fun trackConfirm() {
        analytics.sendEvent(
            "Confirm Clicked",
            "New Widget" to widgetNameModel.newWidget.toString()
        )
    }
}
