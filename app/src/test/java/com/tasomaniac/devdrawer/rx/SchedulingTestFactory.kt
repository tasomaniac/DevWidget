package com.tasomaniac.devdrawer.rx

import io.reactivex.internal.schedulers.TrampolineScheduler

fun testScheduling() = SchedulingStrategy(TrampolineScheduler.instance(), TrampolineScheduler.instance())
