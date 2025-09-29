package com.edts.components.input.search

interface InputSearchDelegate {
    fun onCloseIconClick(inputSearch: InputSearch, clickCount: Int)
    fun onFocusChange(inputSearch: InputSearch, hasFocus: Boolean, newState: InputSearch.State, previousState: InputSearch.State)
    fun onSearchTextChange(inputSearch: InputSearch, text: String, changeCount: Int)
    fun onSearchFieldClick(inputSearch: InputSearch, clickCount: Int)
    fun onSearchSubmit(inputSearch: InputSearch, query: String, searchCount: Int)
    fun onStateChange(inputSearch: InputSearch, newState: InputSearch.State, oldState: InputSearch.State)
}