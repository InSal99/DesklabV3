package com.edts.components.tab

interface TabDelegate {
    fun onTabClick(tabItem: TabItem, newState: TabItem.TabState, previousState: TabItem.TabState)
}