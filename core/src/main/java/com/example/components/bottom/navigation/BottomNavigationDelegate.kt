package com.example.components.bottom.navigation

interface BottomNavigationDelegate {
    fun onBottomNavigationItemClicked(
        item: BottomNavigationItem,
        position: Int = -1,
        clickCount: Int
    )
    fun onBottomNavigationItemStateChanged(
        item: BottomNavigationItem,
        newState: BottomNavigationItem.NavState,
        oldState: BottomNavigationItem.NavState
    )
}