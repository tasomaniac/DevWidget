package com.tasomaniac.devwidget.extensions

import io.reactivex.internal.schedulers.TrampolineScheduler

fun testScheduling() =
    SchedulingStrategy(
        TrampolineScheduler.instance(),
        TrampolineScheduler.instance()
    )
