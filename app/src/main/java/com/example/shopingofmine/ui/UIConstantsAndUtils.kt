package com.example.shopingofmine.ui

import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

inline fun <T> Fragment.collectFlow(flow: Flow<T>,crossinline action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                action.invoke(it)
            }
        }
    }
}

inline fun Fragment.buildAndShowErrorDialog(message: String?, title: String = "خطا",crossinline retry: () -> Unit){
    val alertDialog: AlertDialog? = activity?.let {
        AlertDialog.Builder(it)
    }?.setMessage(message)
        ?.setTitle(title)
        ?.setPositiveButton("تلاش مجدد") { _, _ ->
            retry()
        }
        ?.setNegativeButton("انصراف") { _, _ ->
        }?.create()
    alertDialog?.show()
}

