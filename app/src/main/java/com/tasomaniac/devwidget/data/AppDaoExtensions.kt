package com.tasomaniac.devwidget.data

import io.reactivex.Completable

fun AppDao.deleteApp(packageName: String) =
    Completable.fromAction {
        deleteAppSync(packageName)
    }

fun AppDao.deleteAppsByPackageMatcher(packageMatcher: String) =
    Completable.fromAction {
        deleteAppsByPackageMatcherSync(packageMatcher)
    }
