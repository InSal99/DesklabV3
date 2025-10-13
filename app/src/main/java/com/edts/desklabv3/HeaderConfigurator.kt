package com.edts.desklabv3

interface HeaderConfigurator {
    fun configureHeader(
        title: String? = null,
        subtitle: String? = null,
        showLeftButton: Boolean = true,
        showRightButton: Boolean = false,
        rightButtonIconRes: Int? = null,
        onLeftClick: (() -> Unit)? = null,
        onRightClick: (() -> Unit)? = null,
        isVisible: Boolean = true
    )
}