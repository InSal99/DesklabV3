package com.edts.components.utils

import androidx.viewpager2.widget.ViewPager2
import com.edts.components.tab.Tab

fun Tab.setupWithViewPager2(viewPager2: ViewPager2) {
    this.setOnTabClickListener(object : Tab.OnTabClickListener {
        override fun onTabClick(position: Int, tabText: String) {
            if (viewPager2.currentItem != position) {
                viewPager2.setCurrentItem(position, true)
            }
        }
    })

    viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (this@setupWithViewPager2.getSelectedPosition() != position) {
                this@setupWithViewPager2.setSelectedPosition(position, false)
            }
        }
    })
}