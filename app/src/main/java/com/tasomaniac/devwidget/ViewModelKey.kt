package com.tasomaniac.devwidget

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@MapKey
@Retention(RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)
