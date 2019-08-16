package com.carlisle.seed.support.extension

import android.annotation.SuppressLint
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

@SuppressLint("WrongConstant")
fun AppCompatActivity.hideSoftKeyboard() {
    if (this.currentFocus != null) {
        val manager = this.getSystemService("input_method") as InputMethodManager
        manager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 2)
    }

}

@SuppressLint("WrongConstant")
fun AppCompatActivity.showSoftKeyboard() {
    val imm = this.getSystemService("input_method") as InputMethodManager
    imm.toggleSoftInput(0, 2)
}