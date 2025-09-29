# Input Search

| Feature / Variation | Preview |
| ------------------- | ------- |
| REST State | ![Search input with subtle stroke and placeholder text] |
| FOCUS State | ![Search input with accent stroke and active cursor] |
| ERROR State | ![Search input with red attention stroke] |
| DISABLED State | ![Greyed out search input with disabled appearance] |

| **State** | **Visual Effect** |
| --------- | ----------------- |
| **REST** | Default subtle stroke, enabled interaction |
| **FOCUS** | Accent color stroke, keyboard visible |
| **ERROR** | Attention/red stroke indicating validation error |
| **DISABLED** | Greyed out background, disabled interaction |

## Overview

*A comprehensive search input component with multiple visual states, real-time text monitoring, keyboard handling, and clear functionality. Features debounced interactions and extensive event delegation for search workflows.*

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
    override fun onSearchTextChange(inputSearch: InputSearch, text: String, changeCount: Int) {
        // Handle real-time search text changes
        performLiveSearch(text)
    }
    
    override fun onSearchSubmit(inputSearch: InputSearch, query: String, searchCount: Int) {
        // Handle search submission (Enter key or search action)
        executeSearch(query)
    }
    
    override fun onCloseIconClick(inputSearch: InputSearch, clickCount: Int) {
        // Handle clear button clicks
        clearSearchResults()
    }
    
    override fun onFocusChange(inputSearch: InputSearch, hasFocus: Boolean, newState: InputSearch.State, previousState: InputSearch.State) {
        // Handle focus state changes
        if (hasFocus) showSearchSuggestions() else hideSearchSuggestions()
    }
    
    override fun onSearchFieldClick(inputSearch: InputSearch, clickCount: Int) {
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

## Data Models 

### InputSearchDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onSearchTextChange()` | `inputSearch: InputSearch, text: String, changeCount: Int` | ✅ | Called on every text change |
| `onSearchSubmit()` | `inputSearch: InputSearch, query: String, searchCount: Int` | ✅ | Called when search is submitted |
| `onCloseIconClick()` | `inputSearch: InputSearch, clickCount: Int` | ✅ | Called when clear button is clicked |
| `onFocusChange()` | `inputSearch: InputSearch, hasFocus: Boolean, newState: State, previousState: State` | ✅ | Called on focus state changes |
| `onSearchFieldClick()` | `inputSearch: InputSearch, clickCount: Int` | ✅ | Called when search field is clicked |
| `onStateChange()` | `inputSearch: InputSearch, newState: State, oldState: State` | ✅ | Called on programmatic state changes |

```kotlin
val searchDelegate = object : InputSearchDelegate {
    override fun onSearchTextChange(inputSearch: InputSearch, text: String, changeCount: Int) {
        // Real-time search implementation
        searchViewModel.updateQuery(text)
    }
    
    override fun onSearchSubmit(inputSearch: InputSearch, query: String, searchCount: Int) {
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
| `getCloseIconClickCount()` | None | Get close icon click count for analytics |
| `getSearchFieldClickCount()` | None | Get search field click count |
| `getSearchTextChangeCount()` | None | Get text change count |
| `getSearchSubmitCount()` | None | Get search submission count |
| `resetCloseIconClickCount()` | None | Reset close icon click counter |
| `resetSearchFieldClickCount()` | None | Reset search field click counter |
| `resetSearchTextChangeCount()` | None | Reset text change counter |
| `resetSearchSubmitCount()` | None | Reset search submit counter |
| `resetAllCounts()` | None | Reset all interaction counters |

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

// Analytics and debugging
val totalInteractions = binding.searchInput.getSearchTextChangeCount()
binding.searchInput.resetAllCounts()
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
    override fun onSearchTextChange(inputSearch: InputSearch, text: String, changeCount: Int) {
        if (text.length > 100) {
            inputSearch.state = InputSearch.State.ERROR
            showError("Query too long")
        } else {
            inputSearch.state = InputSearch.State.REST
            performLiveSearch(text)
        }
    }
    
    override fun onSearchSubmit(inputSearch: InputSearch, query: String, searchCount: Int) {
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
    
    override fun onCloseIconClick(inputSearch: InputSearch, clickCount: Int) {
        clearSearchResults()
        hideKeyboard()
        analytics.track("search_cleared", mapOf("click_count" to clickCount))
    }
    
    // ... other implementations
}

// Dynamic placeholder updates
fun updateSearchContext(category: String) {
    binding.searchInput.binding.etSearch.hint = "Search in $category..."
}
```

## Performance Considerations

- **Click Debouncing** — Built-in 300ms debounce prevents rapid successive interactions and improves user experience
- **Color Caching** — Theme colors are resolved once and cached to avoid repeated attribute lookups during state changes
- **ViewBinding** — Uses ViewBinding for efficient view access and type safety
- **Text Watching** — Optimized TextWatcher implementation for real-time search without excessive callbacks

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Implement comprehensive InputSearchDelegate for full functionality | Leave delegate methods empty without proper handling |
| Use debounced search for live search to avoid excessive API calls | Trigger search on every character without delay |
| Clear focus and hide keyboard after search submission | Leave keyboard open after search completion |
| Handle all state transitions appropriately | Ignore state changes or validation requirements |
| Use error state for validation feedback | Show errors through external UI only |
| Reset counters periodically for accurate analytics | Let interaction counters grow indefinitely |
| Test keyboard interactions across different devices | Assume keyboard behavior is consistent |

---

> **⚠️ Note**: This component manages keyboard visibility automatically and includes comprehensive interaction tracking for analytics. The close icon appears only when text is present, and all interactions are debounced to prevent accidental rapid-fire events. State management preserves previous states for proper focus restoration.