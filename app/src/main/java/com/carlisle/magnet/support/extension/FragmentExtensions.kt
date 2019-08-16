package com.carlisle.seed.support.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.jetbrains.anko.bundleOf

inline fun <reified T : Fragment> instanceOf(vararg params: Pair<String, Any>) = T::class.java.newInstance().apply {
    arguments = bundleOf(*params)
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}