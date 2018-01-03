package com.tasomaniac.devdrawer

import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import io.reactivex.internal.schedulers.TrampolineScheduler

fun testScheduling() = SchedulingStrategy(TrampolineScheduler.instance(), TrampolineScheduler.instance())
