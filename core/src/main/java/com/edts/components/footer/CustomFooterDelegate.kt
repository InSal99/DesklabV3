package com.edts.components.footer

interface CustomFooterDelegate {
    fun onPrimaryButtonClicked(footerType: CustomFooter.FooterType)
    fun onSecondaryButtonClicked(footerType: CustomFooter.FooterType)

    fun onRegisterClicked()
    fun onCancelClicked()
    fun onContinueClicked()
}