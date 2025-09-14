package com.edts.components.tab

data class TabData(
    val text: String,
    val badgeText: String? = null,
    val showBadge: Boolean = true,
    val state: TabItem.TabState = TabItem.TabState.INACTIVE
)
