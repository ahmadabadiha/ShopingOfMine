package com.example.shopingofmine.ui

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
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

/*fun createDialog(activity: FragmentActivity?, message: String, title: String, positiveButton: String, negativeButton: String) {
    val alertDialog: AlertDialog? = activity?.let {
        AlertDialog.Builder(it)
    }?.setMessage(message)
        ?.setTitle("خطا")
        ?.setPositiveButton("تلاش مجدد") { _, _ ->
            viewModel.getProducts()
        }
        ?.setNegativeButton("انصراف") { _, _ ->
        }?.create()
    alertDialog?
}*/
private fun <T> StateFlow<T>.colledctIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collectLatest {
                function.invoke(it)
            }
        }
    }
}