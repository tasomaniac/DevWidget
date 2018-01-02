package com.tasomaniac.devdrawer.configure

import android.content.Intent
import android.content.pm.PackageManager
import android.support.annotation.VisibleForTesting
import io.reactivex.Observable
import javax.inject.Inject

class ConfigureUseCase @Inject constructor(private val packageManager: PackageManager) {

  fun findExistingPackages(): Observable<Collection<String>> {
    return Observable.fromCallable {
      val intent = Intent(Intent.ACTION_MAIN)
          .addCategory(Intent.CATEGORY_LAUNCHER)
      findExistingPackagesSync(intent)
    }
  }

  @VisibleForTesting
  fun findExistingPackagesSync(intent: Intent): Set<String> {
    val intentActivities = packageManager.queryIntentActivities(intent, 0)

    return intentActivities
        .map { it.activityInfo.applicationInfo.packageName }
        .toSortedSet()
        .flatMap {
          var packageName = it

          it.split('.')
              .foldRight(setOf(it)) { part, acc ->
                packageName = packageName.removeSuffix(".$part")
                acc + "$packageName.*"
              }
              .reversed()
        }
        .toSet()
  }
}
