package com.tasomaniac.devwidget.widget.chooser

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.extensions.inflate
import com.tasomaniac.devwidget.widget.DisplayResolveInfo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.resolve_list_item.*
import javax.inject.Inject

class ActivityChooserViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(info: DisplayResolveInfo) {
        text1.text = info.label
        text2.text = info.secondaryLabel
        icon.setImageDrawable(info.icon)
    }

    class Factory @Inject constructor() {

        private val creator = { view: View ->
            ActivityChooserViewHolder(view)
        }

        fun createWith(parent: ViewGroup) = creator(
            parent.inflate(R.layout.resolve_list_item)
        )
    }
}
