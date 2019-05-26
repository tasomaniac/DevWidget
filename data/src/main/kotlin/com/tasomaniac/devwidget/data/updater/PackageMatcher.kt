package com.tasomaniac.devwidget.data.updater

import io.reactivex.functions.Predicate

fun matchPackage(packageMatcher: String) = object : Predicate<String> {
    private val starFilter = packageMatcher.endsWith(".*")
    private val packageMatcherWithoutStar = packageMatcher.removeSuffix(".*")

    override fun test(value: String) = if (starFilter) {
        value.startsWith(packageMatcherWithoutStar, ignoreCase = true)
    } else {
        value.equals(packageMatcher, ignoreCase = true)
    }
}
