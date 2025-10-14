# Modality Confirmation

A standardized confirmation dialog component for critical user actions, available as both a pop-up dialog and static view with consistent design and behavior.

## Visual Anatomy

![Modality Confirmation](https://res.cloudinary.com/fauzanspratama/image/upload/c_scale,w_480/v1759305056/Modality_Confirmation_jx0hp7.gif)

| Element | Description |
| :------ | :---------- |
| **Title** | Primary heading using `h1SemiBold` typography |
| **Description** | Detailed message using `p1Regular` typography |
| **Close Button** | Secondary action button for cancellation |
| **Confirm Button** | Primary action button for confirmation |

## Key Features
- **Dual Usage**: Available as pop-up dialog (`ModalityConfirmationPopUp`) and static view (`EventModalityConfirmation`)
- **Consistent Design**: Standardized UI across all confirmation prompts
- **Simple API**: Single `show()` method for dialog with comprehensive configuration
- **Callback-Driven**: Lambda functions for clean event handling
- **Customizable Content**: Flexible text for all elements via parameters or XML attributes
- **Dismissibility Control**: Option to prevent accidental dismissal

## Pop-Up Dialog Implementation

### Kotlin Usage
```kotlin
ModalityConfirmationPopUp.show(
    context = requireContext(),
    title = "Konfirmasi Undangan",
    description = "Apakah kamu yakin terima undangan dan akan menghadiri event ini nanti?",
    confirmButtonLabel = "Ya, Lanjutkan",
    closeButtonLabel = "Tutup",
    isDismissible = false, // Prevent accidental dismissal
    onConfirm = {
        // Handle confirmation
        confirmEventInvitatiton()
    },
    onClose = {
        // Handle cancellation
        showCancellationMessage()
    }
)
```

#### Parameters
| Parameter | Type | Description |
| :-------- | :--- | :---------- |
| `context` | `Context` | Context for dialog display |
| `title` | `String` | Dialog title text |
| `description` | `String` | Main descriptive message |
| `confirmButtonLabel` | `String` | Primary action button text |
| `closeButtonLabel` | `String` | Secondary action button text |
| `isDismissible` | `Boolean` | Allow dismissal via back button or outside tap |
| `onConfirm` | `() -> Unit` | Lambda executed on confirmation |
| `onClose` | `() -> Unit` | Lambda executed on close |

## Static View Implementation

### XML Usage
```xml
<com.edts.components.modal.EventModalityConfirmation
    android:id="@+id/static_confirmation"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:modalTitle="Konfirmasi Undangan"
    app:modalDescription="Apakah kamu yakin terima undangan dan akan menghadiri event ini nanti?"
    app:modalConfirmButtonLabel="Ya, Lanjutkan"
    app:modalCloseButtonLabel="Tutup" />
```

#### XML Attributes
| Attribute | Format | Description |
| :-------- | :----- | :---------- |
| `modalTitle` | `string` | Component title |
| `modalDescription` | `string` | Description text |
| `modalConfirmButtonLabel` | `string` | Primary button label |
| `modalCloseButtonLabel` | `string` | Secondary button label |

### Event Handling for Static View
```kotlin
class MyFragment : Fragment(), EventModalityConfirmationDelegate {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.staticConfirmation.delegate = this
    }

    override fun onConfirmClick(modality: EventModalityConfirmation) {
        // Handle confirmation
        positiveAction()
    }

    override fun onCloseClick(modality: EventModalityConfirmation) {
        // Handle close
        negativeAction()
    }
}
```

#### Memory Management
```kotlin
override fun onDestroyView() {
    binding.staticConfirmation.delegate = null
    super.onDestroyView()
}
```

## Do's and Don'ts

### ✅ Do's
- Use for critical actions requiring user confirmation
- Provide clear, action-oriented button labels
- Make destructive actions clearly identifiable
- Use non-dismissible dialogs for critical operations
- Keep titles concise and descriptions informative
- Test both pop-up and static view scenarios

### ❌ Don'ts
- Use for non-critical or reversible actions
- Use vague button labels like "OK" or "Yes"
- Override the default button colors unnecessarily
- Block user interaction without clear escape path
- Use excessively long descriptions
- Ignore accessibility for screen readers

## Performance Considerations

- **Dialog Lifecycle**: Proper context handling to prevent leaks
- **View Binding**: Efficient view inflation with binding
- **Memory Management**: Clear delegates in lifecycle methods
- **Error Handling**: Try-catch around dialog creation

```kotlin
// Safe dialog showing with error handling
try {
    ModalityConfirmationPopUp.show(...)
} catch (e: Exception) {
    Log.e("ModalityConfirmation", "Dialog error", e)
    // Fallback to alternative UI
}
```

## Material Design Styling

The component implements Material Design with:

- **Card Layout**: `MaterialCardView` with 16dp corner radius
- **Elevation**: 2dp elevation for visual hierarchy
- **Stroke**: 1dp subtle border using `colorStrokeSubtle`
- **Typography**: 
  - Title: `h1SemiBold` for prominence
  - Description: `p1Regular` for readability
- **Button Styles**: Pre-styled primary and secondary buttons
- **Shadow**: Dynamic shadow colors for API 28+
- **Theme Integration**: Uses theme attributes for consistent theming

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
>This component provides a consistent confirmation experience across the application. Use the pop-up version for transient confirmations and the static view for permanent confirmation sections within screens. Both implementations maintain the same visual design and interaction patterns.
