package com.hin.movie.utils.extensions

import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.hin.movie.ui.base.SpaceItemDecoration

fun RecyclerView.setupFlexbox(justifyC: Int = JustifyContent.FLEX_START, flexD: Int = FlexDirection.ROW, gap: Int = 24) {
    layoutManager = FlexboxLayoutManager(context).apply {
        justifyContent = justifyC
        flexDirection = flexD
    }
    addItemDecoration(SpaceItemDecoration(gap))
}
