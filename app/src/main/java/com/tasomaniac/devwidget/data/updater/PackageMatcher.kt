package com.tasomaniac.devwidget.data.updater

import io.reactivex.functions.Predicate

fun matchPackage(packageMatcher: String) = Predicate<String> {

    if (packageMatcher.endsWith(".*")) {
        it.startsWith(packageMatcher.removeSuffix(".*"), ignoreCase = true)
    } else {
        it.equals(packageMatcher, ignoreCase = true)
    }
}
