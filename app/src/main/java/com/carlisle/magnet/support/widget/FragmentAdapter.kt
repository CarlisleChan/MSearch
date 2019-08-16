package com.carlisle.magnet.support.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class FragmentAdapter : FragmentStatePagerAdapter {
    private var fragments: List<Fragment> = ArrayList()
    private var titles: List<String> = ArrayList()

    constructor(fm: FragmentManager) : super(fm) {}

    constructor(fm: FragmentManager, fragments: List<Fragment>) : super(fm) {
        this.fragments = fragments
    }

    constructor(fm: FragmentManager, fragments: List<Fragment>, titles: List<String>) : super(fm) {
        this.fragments = fragments
        this.titles = titles
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (this.titles.isEmpty()) "" else this.titles[position]
    }

    override fun getItem(position: Int): Fragment {
        return this.fragments[position]
    }

    override fun getCount(): Int {
        return this.fragments.size
    }

}