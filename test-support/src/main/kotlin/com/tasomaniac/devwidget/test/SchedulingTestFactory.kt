package com.tasomaniac.devwidget.test

import com.tasomaniac.devwidget.extensions.SchedulingStrategy
import io.reactivex.internal.schedulers.TrampolineScheduler

fun testScheduling() =
    SchedulingStrategy(
        TrampolineScheduler.instance(),
        TrampolineScheduler.instance()
    )
