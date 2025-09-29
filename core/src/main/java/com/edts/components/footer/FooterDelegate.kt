package com.edts.components.footer

interface FooterDelegate {
    fun onPrimaryButtonClicked(footerType: Footer.FooterType)
    fun onSecondaryButtonClicked(footerType: Footer.FooterType)
    fun onRegisterClicked()
    fun onCancelClicked()
    fun onContinueClicked()
}