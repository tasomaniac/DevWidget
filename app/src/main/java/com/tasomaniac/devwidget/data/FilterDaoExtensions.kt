package com.tasomaniac.devwidget.data

import io.reactivex.Completable

fun FilterDao.deletePackageMatcher(packageMatcher: String) =
    Completable.fromAction {
        deleteFilterSync(packageMatcher)
    }
