package com.tasomaniac.devdrawer.configure

import android.support.v7.widget.RecyclerView
import android.widget.TextView

class PackageMatcherViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
  fun bind(packageMatcher: String) {
    textView.text = packageMatcher
  }
}
