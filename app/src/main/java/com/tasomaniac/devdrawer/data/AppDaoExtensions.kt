package com.tasomaniac.devdrawer.data

import io.reactivex.Completable

fun AppDao.deleteAppsByPackageMatcher(packageMatcher: String): Completable =
    Completable.fromAction {
      deleteAppsByPackageMatcherSync(packageMatcher)
    }
