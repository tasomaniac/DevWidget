package com.tasomaniac.devwidget.rx

import io.reactivex.internal.schedulers.TrampolineScheduler

fun testScheduling() = SchedulingStrategy(TrampolineScheduler.instance(), TrampolineScheduler.instance())
