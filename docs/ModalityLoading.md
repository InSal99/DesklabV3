# Modality Loading

A modal loading dialog component that provides user feedback during asynchronous operations, blocking UI interaction while processes complete.

## Visual Anatomy

![Modality Loading](https://res.cloudinary.com/fauzanspratama/image/upload/c_scale,w_480/v1759304413/Modality_Loading_kvx2qc.gif)

| Element | Description |
| :------ | :---------- |
| **Progress Indicator** | Circular progress indicator with accent color |
| **Title** | Customizable loading message using `h1SemiBold` typography |

## Key Features
- **UI Blocking**: Prevents user interaction during background tasks
- **Clear Visual Feedback**: Circular progress indicator with customizable message
- **Simple API**: Single `show()` method returns dismissible dialog instance
- **Cancelable Option**: Configurable back button and outside tap behavior
- **Dual Usage**: Available as pop-up dialog and static view
- **Theme Integration**: Follows app theme colors and styling

## Pop-Up Dialog Implementation

### Kotlin Usage
```kotlin
private var loadingDialog: AlertDialog? = null

private fun performNetworkRequest() {
    // Show loading dialog
    loadingDialog = ModalityLoadingPopUp.show(
        context = requireContext(),
        title = "Tunggu sebentar...",
        isCancelable = false
    )

    // Execute background task
    lifecycleScope.launch {
        try {
            val result = apiService.fetchData()
            handleResult(result)
        } catch (e: Exception) {
            showError(e)
        } finally {
            // Always dismiss when done
            loadingDialog?.dismiss()
        }
    }
}
```

#### Parameters
| Parameter | Type | Description |
| :-------- | :--- | :---------- |
| `context` | `Context` | Context for dialog display |
| `title` | `String` | Loading message text |
| `isCancelable` | `Boolean` | Allow dismissal via back button or outside tap |

**Returns**: `AlertDialog?` - Dialog instance for programmatic dismissal

## Static View Implementation

### XML Usage
```xml
<com.edts.components.modal.EventModalityLoading
    android:id="@+id/loading_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:modalLoadingTitle="Tunggu sebentar..." />
```

#### XML Attributes
| Attribute | Format | Description |
| :-------- | :----- | :---------- |
| `modalLoadingTitle` | `string` | Loading message text |

### Programmatic Control
```kotlin
// Show/hide static loading view
binding.loadingView.isVisible = true
binding.loadingView.title = "Tunggu sebentar..."

// When done
binding.loadingView.isVisible = false
```

## Advanced Usage Patterns

### Coroutine Integration
```kotlin
private suspend fun <T> withLoading(
    message: String,
    block: suspend () -> T
): T {
    val dialog = ModalityLoadingPopUp.show(requireContext(), message, false)
    return try {
        block()
    } finally {
        dialog?.dismiss()
    }
}

// Usage
lifecycleScope.launch {
    val data = withLoading("Loading user profile...") {
        userRepository.getProfile()
    }
    updateUI(data)
}
```

## Do's and Don'ts

### ✅ Do's
- Use for operations taking more than 1-2 seconds
- Provide specific, actionable loading messages
- Make non-cancelable for critical operations
- Always dismiss in `finally` block to prevent stuck dialogs
- Use with coroutines or background threads
- Test both success and error dismissal scenarios

### ❌ Don'ts
- Use for instant or very short operations
- Use vague messages like "Loading..." when possible
- Allow cancellation for operations that leave data inconsistent
- Forget to handle configuration changes (use ViewModel)
- Block the main thread while showing loading
- Show multiple loading dialogs simultaneously

## Performance Considerations

### Memory Management
```kotlin
// Store dialog reference properly
private var loadingDialog: AlertDialog? = null

override fun onDestroy() {
    loadingDialog?.dismiss()
    loadingDialog = null
    super.onDestroy()
}
```

## Material Design Styling

The component implements Material Design with:

- **Progress Indicator**: 
  - 44dp circular indicator
  - `colorForegroundAccentPrimaryIntense` for active progress
  - `colorBackgroundTertiary` for track background
  - Rounded track corners using `radius_999dp`
- **Typography**: `h1SemiBold` for loading message
- **Card Layout**: 
  - 16dp corner radius
  - 2dp elevation
  - 1dp subtle border using `colorStrokeSubtle`
  - Primary background color
- **Shadow**: Dynamic shadow colors for API 28+

### Customization
```xml
<!-- Modality Dialog -->  
<style name="Theme.App.Dialog.Confirmation" parent="ThemeOverlay.MaterialComponents.MaterialAlertDialog">  
    <item name="alertDialogStyle">@style/Style.App.Dialog.Confirmation</item>  
</style>  
  
<style name="Style.App.Dialog.Confirmation" parent="MaterialAlertDialog.MaterialComponents">  
    <item name="backgroundTint">?attr/colorBackgroundPrimary</item>  
    <item name="shapeAppearance">@style/ShapeAppearance.App.Dialog.Custom</item>  
</style>  
  
<style name="ShapeAppearance.App.Dialog.Custom" parent="ShapeAppearance.MaterialComponents.MediumComponent">  
    <item name="cornerFamily">rounded</item>  
    <item name="cornerSize">16dp</item>  
</style>
```

---

>[!Note]
>Use this component for operations requiring user patience. The pop-up version is ideal for transient operations, while the static view works well for persistent loading states within screens. Always ensure proper dismissal to maintain good user experience.
