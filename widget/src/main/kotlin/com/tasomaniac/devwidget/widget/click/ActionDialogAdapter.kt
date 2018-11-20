package com.tasomaniac.devwidget.widget.click

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tasomaniac.devwidget.widget.R

class ActionDialogAdapter(
    context: Context,
    data: List<Action>
) : ArrayAdapter<Action>(
    context, R.layout.click_handling_action_dialog, data
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        (super.getView(position, convertView, parent) as TextView).apply {
            val item = getItem(position)!!

            setText(item.text)
            if (item.icon != null) {
                setCompoundDrawablesRelativeWithIntrinsicBounds(item.icon, 0, 0, 0)
            } else {
                setCompoundDrawables(null, null, null, null)
            }
        }
}
