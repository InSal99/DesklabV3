package com.edts.desklabv3

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//class TabFragmentAdapter(
//    fragmentActivity: FragmentActivity,
//    private val fragments: List<Fragment>
//) : FragmentStateAdapter(fragmentActivity) {
//
//    override fun getItemCount(): Int = fragments.size
//    override fun createFragment(position: Int): Fragment = fragments[position]
//}

class TabFragmentAdapter : FragmentStateAdapter {
    private val fragments: List<Fragment>

    constructor(fragment: Fragment, fragments: List<Fragment>) : super(fragment) {
        this.fragments = fragments
    }

    constructor(fragmentActivity: FragmentActivity, fragments: List<Fragment>) : super(fragmentActivity) {
        this.fragments = fragments
    }

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
