package ru.netology.nmedia.utils

import android.content.Context
import android.view.InputDevice
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

//    fun showKeyboard(view: View) {
//        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(view.rootView, InputMethodManager.SHOW_IMPLICIT)
//    }

    fun focusAndShowKeyboard(view: View) {

        fun View.showTheKeyboardNow() {
            val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }

        view.requestFocus()
        if (view.hasWindowFocus()) {
            view.showTheKeyboardNow()
        } else {
            view.viewTreeObserver.addOnWindowFocusChangeListener(
                object : ViewTreeObserver.OnWindowFocusChangeListener {
                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        if (hasFocus) {
                            view.showTheKeyboardNow()
                            view.viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                })
        }
    }
}