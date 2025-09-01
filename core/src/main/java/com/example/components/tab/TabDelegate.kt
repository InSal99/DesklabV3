package com.example.components.tab

interface TabDelegate {
    fun onTabClick(tab: Tab, newState: Tab.TabState, previousState: Tab.TabState)
}