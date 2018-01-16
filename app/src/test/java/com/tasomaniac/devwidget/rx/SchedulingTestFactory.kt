package com.tasomaniac.devwidget.rx

import com.tasomaniac.devwidget.rx.SchedulingStrategy
import io.reactivex.internal.schedulers.TrampolineScheduler

fun testScheduling() = SchedulingStrategy(TrampolineScheduler.instance(), TrampolineScheduler.instance())
