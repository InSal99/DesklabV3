package com.example.components.footer

interface CustomFooterDelegate {
    fun onPrimaryButtonClicked(footerType: CustomFooter.FooterType)
    fun onSecondaryButtonClicked(footerType: CustomFooter.FooterType)

    // Specific action callbacks with default implementations
    fun onRegisterClicked() {}
    fun onCancelClicked() {}
    fun onContinueClicked() {}
    fun onAttendanceInfoClicked() {}

    // Advanced callbacks
    fun onButtonLongPressed(footerType: CustomFooter.FooterType, isPrimary: Boolean) {}
    fun onFooterStateChanged(footerType: CustomFooter.FooterType, isEnabled: Boolean) {}
}