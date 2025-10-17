package com.edts.components.input.search

interface InputSearchDelegate {
    fun onCloseIconClick(inputSearch: InputSearch)
    fun onFocusChange(inputSearch: InputSearch, hasFocus: Boolean, newState: InputSearch.State, previousState: InputSearch.State)
    fun onSearchTextChange(inputSearch: InputSearch, text: String)
    fun onSearchFieldClick(inputSearch: InputSearch)
    fun onSearchSubmit(inputSearch: InputSearch, query: String)
    fun onStateChange(inputSearch: InputSearch, newState: InputSearch.State, oldState: InputSearch.State)
}