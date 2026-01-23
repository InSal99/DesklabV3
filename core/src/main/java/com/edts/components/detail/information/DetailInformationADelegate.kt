package com.edts.components.detail.information

interface DetailInformationADelegate {
    fun onAction1Clicked(view: DetailInformationA)
    fun onAction2Clicked(view: DetailInformationA)
    fun onDescIconClick(view: DetailInformationA)
    fun onItemClick(view: DetailInformationA)
    fun onUrlClicked(view: DetailInformationA, url: String)
}