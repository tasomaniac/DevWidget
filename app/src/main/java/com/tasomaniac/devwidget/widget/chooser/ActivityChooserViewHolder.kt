package com.tasomaniac.devwidget.widget.chooser

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.resolve_list_item.icon
import kotlinx.android.synthetic.main.resolve_list_item.text1
import kotlinx.android.synthetic.main.resolve_list_item.text2
import javax.inject.Inject

class ActivityChooserViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(info: DisplayResolveInfo, itemClickListener: (DisplayResolveInfo) -> Unit) {
        text1.text = info.label
        text2.text = info.component.shortClassName
        icon.setImageDrawable(info.icon)
        itemView.setOnClickListener {
            itemClickListener(info)
        }
    }

    class Factory @Inject constructor() {

        private val creator = { view: View ->
            ActivityChooserViewHolder(view)
        }

        fun createWith(parent: ViewGroup) = creator(parent.inflate(R.layout.resolve_list_item))
    }
}
