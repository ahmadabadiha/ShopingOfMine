package com.example.shopingofmine.ui

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import java.util.*

const val NOTIFICATION_CHANNEL_ID = "shopping channel"

fun ViewGroup.rtl() {
    val stack = Stack<View>()
    stack.add(this)
    while (stack.isNotEmpty()) {
        stack.pop().apply {
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            textDirection = View.TEXT_DIRECTION_RTL
            (this as? ViewGroup)?.children?.forEach { stack.add(it) }
        }
    }
}