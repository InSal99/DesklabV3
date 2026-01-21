# Input Search

| Feature / Variation | Preview                                                                                                                                         |
| ------------------- |-------------------------------------------------------------------------------------------------------------------------------------------------|
| REST State | ![Search input with subtle stroke and placeholder text](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287436/is_rest_state_fp64po.gif) |
| FOCUS State | ![Search input with accent stroke and active cursor](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287421/is_focus_state_lmzqtt.png)                                                                                          |
| ERROR State | ![Search input with red attention stroke](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287435/is_error_state_nfyquc.gif)              |
| DISABLED State | ![Greyed out search input with disabled appearance](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287433/is_disabled_state_hlrqug.png) |

| **State** | **Visual Effect** |
| --------- | ----------------- |
| **REST** | Default subtle stroke, enabled interaction |
| **FOCUS** | Accent color stroke, keyboard visible |
| **ERROR** | Attention/red stroke indicating validation error |
| **DISABLED** | Greyed out background, disabled interaction |

## Overview

*A comprehensive search input component with multiple visual states, real-time text monitoring, keyboard handling, and clear functionality.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.input.search.InputSearch
    android:id="@+id/inputSearch"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:inputSearchHint="Search products, brands, categories..."
    app:inputSearchState="rest" />
```

### 2. Initialize in Code

```kotlin
val inputSearch = binding.inputSearch

// Set up delegate for comprehensive event handling
inputSearch.delegate = object : InputSearchDelegate {
    override fun onSearchTextChange(inputSearch: InputSearch, text: String) {
        // Handle real-time search text changes
        performLiveSearch(text)
    }
    
    override fun onSearchSubmit(inputSearch: InputSearch, query: String) {
        // Handle search submission (Enter key or search action)
        executeSearch(query)
    }
    
    override fun onCloseIconClick(inputSearch: InputSearch) {
        // Handle clear button clicks
        clearSearchResults()
    }
    
    override fun onFocusChange(inputSearch: InputSearch, hasFocus: Boolean, newState: InputSearch.State, previousState: InputSearch.State) {
        // Handle focus state changes
        if (hasFocus) showSearchSuggestions() else hideSearchSuggestions()
    }
    
    override fun onSearchFieldClick(inputSearch: InputSearch) {
        // Handle search field clicks
        logSearchFieldInteraction()
    }
    
    override fun onStateChange(inputSearch: InputSearch, newState: InputSearch.State, oldState: InputSearch.State) {
        // Handle programmatic state changes
        updateUIForState(newState)
    }
}
```

## Display Modes 

| State Name | Value | Description | Use Case |
| ---------- | ----- | ----------- | -------- |
| `REST` | `0` | Default inactive state with subtle styling | Normal display, not focused |
| `FOCUS` | `1` | Active state with accent border and keyboard | User is actively typing |
| `DISABLE` | `2` | Disabled state preventing all interaction | Feature unavailable/loading |
| `ERROR` | `3` | Error state with attention border styling | Validation failed or search error |

```kotlin
// Set state programmatically
inputSearch.state = InputSearch.State.ERROR
inputSearch.state = InputSearch.State.FOCUS
```

## Properties Reference

### Text Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `inputSearchHint` | `String` | `"Search Placeholder"` | Placeholder text displayed when empty |

### State Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `inputSearchState` | `String` | `"rest"` | XML attribute for initial state |
| `state` | `State` | `State.REST` | Current visual/interaction state |
| `delegate` | `InputSearchDelegate?` | `null` | Event handling interface |

### Internal Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `cardState` | `CardState` | `REST` | Touch press state (managed internally) |
| `previousState` | `State` | `REST` | Previous state for focus restoration |

### XML-only Attributes

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `inputSearchHint` | `String` | `"Search Placeholder"` | Placeholder text displayed when empty |
| `inputSearchState` | `enum` | `rest` | Initial state: rest(0), focus(1), disable(2), error(3) |

## Data Models 

### InputSearchDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onSearchTextChange()` | `inputSearch: InputSearch, text: String` | ✅ | Called on every text change |
| `onSearchSubmit()` | `inputSearch: InputSearch, query: String` | ✅ | Called when search is submitted |
| `onCloseIconClick()` | `inputSearch: InputSearch` | ✅ | Called when clear button is clicked |
| `onFocusChange()` | `inputSearch: InputSearch, hasFocus: Boolean, newState: State, previousState: State` | ✅ | Called on focus state changes |
| `onSearchFieldClick()` | `inputSearch: InputSearch` | ✅ | Called when search field is clicked |
| `onStateChange()` | `inputSearch: InputSearch, newState: State, oldState: State` | ✅ | Called on programmatic state changes |

```kotlin
val searchDelegate = object : InputSearchDelegate {
    override fun onSearchTextChange(inputSearch: InputSearch, text: String) {
        // Real-time search implementation
        searchViewModel.updateQuery(text)
    }
    
    override fun onSearchSubmit(inputSearch: InputSearch, query: String) {
        // Final search execution
        searchViewModel.executeSearch(query)
    }
    
    // ... implement other required methods
}
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setText()` | `text: String` | Programmatically set search text |
| `getText()` | None | Get current search text |
| `clearText()` | None | Clear search text |
| `clearFocus()` | None | Remove focus and hide keyboard |

## Usage Examples

```kotlin
// Basic search setup
binding.searchInput.apply {
    delegate = searchDelegate
    setText("initial query")
}

// State management
fun showSearchError(message: String) {
    binding.searchInput.state = InputSearch.State.ERROR
    binding.errorText.text = message
    binding.errorText.visibility = View.VISIBLE
}

fun enableSearch() {
    binding.searchInput.state = InputSearch.State.REST
    binding.errorText.visibility = View.GONE
}

// Text manipulation
binding.searchInput.setText("Product name")
val currentQuery = binding.searchInput.getText()
binding.searchInput.clearText()
```

## Customization Examples

### XML State Variations

```xml
<!-- REST state - default inactive -->
<com.edts.components.input.search.InputSearch
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:inputSearchHint="Search products..."
    app:inputSearchState="rest" />

<!-- ERROR state - validation failed -->
<com.edts.components.input.search.InputSearch
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:inputSearchHint="Search products..."
    app:inputSearchState="error" />

<!-- FOCUS state - active input -->
<com.edts.components.input.search.InputSearch
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:inputSearchHint="Search products..."
    app:inputSearchState="focus" />

<!-- DISABLED state - unavailable -->
<com.edts.components.input.search.InputSearch
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:inputSearchHint="Search unavailable"
    app:inputSearchState="disable" />
```

### Advanced Usage Patterns

```kotlin
// Search with validation
binding.searchInput.delegate = object : InputSearchDelegate {
    override fun onSearchTextChange(inputSearch: InputSearch, text: String) {
        if (text.length > 100) {
            inputSearch.state = InputSearch.State.ERROR
            showError("Query too long")
        } else {
            inputSearch.state = InputSearch.State.REST
            performLiveSearch(text)
        }
    }
    
    override fun onSearchSubmit(inputSearch: InputSearch, query: String) {
        if (query.isBlank()) {
            inputSearch.state = InputSearch.State.ERROR
            return
        }
        
        // Show loading state
        inputSearch.state = InputSearch.State.DISABLE
        executeSearch(query) { results ->
            inputSearch.state = InputSearch.State.REST
            displayResults(results)
        }
    }
    
    override fun onCloseIconClick(inputSearch: InputSearch) {
        clearSearchResults()
        hideKeyboard()
    }
    
    // ... other implementations
}

// Dynamic placeholder updates
fun updateSearchContext(category: String) {
    binding.searchInput.binding.etSearch.hint = "Search in $category..."
}
```

## Performance Considerations

- **ViewBinding** — Uses ViewBinding for efficient view access and type safety
- **Text Watching** — Optimized TextWatcher implementation for real-time search without excessive callbacks
- **MaterialCardView** — Built on Material Design components with 2dp elevation and 12dp corner radius
- **Dynamic Theming** — Uses theme attributes for colors, supporting light/dark modes automatically
- **Ripple Effects** — Transparent ripple on card, custom 12dp radius ripple on close icon

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Implement comprehensive InputSearchDelegate for full functionality | Leave delegate methods empty without proper handling |
| Clear focus and hide keyboard after search submission | Leave keyboard open after search completion |
| Handle all state transitions appropriately | Ignore state changes or validation requirements |
| Use error state for validation feedback | Show errors through external UI only |
| Test keyboard interactions across different devices | Assume keyboard behavior is consistent |

---

> **⚠️ Note**: This component manages keyboard visibility automatically. The close icon appears only when text is present. State management preserves previous states for proper focus restoration.