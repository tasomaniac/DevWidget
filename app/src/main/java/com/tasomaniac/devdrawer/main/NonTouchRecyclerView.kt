package com.tasomaniac.devdrawer.main

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent


class NonTouchRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

  
  override fun onInterceptTouchEvent(ev: MotionEvent) = false

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(e: MotionEvent?) = false

}
