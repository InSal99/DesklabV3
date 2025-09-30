# Modality Confirmation

## Overview
The `ModalityConfirmationPopUp` is a specialized utility object designed to present a standardized confirmation dialog to the user. It simplifies the process of asking for user confirmation for critical actions, such as saving changes, deleting data, or proceeding with a specific task.

The component is built to be straightforward, ensuring a consistent user experience across the application by providing a pre-defined layout consisting of a title, a descriptive message, and two distinct action buttons (Confirm and Close).

## Visual Preview
The dialog presents a clean and focused interface for user decisions. It consists of a title, a description, and two horizontally aligned action buttons: a primary confirmation button and a secondary close/cancel button.

## Key Features
- **Standardized UI**: Enforces a consistent design for all confirmation prompts by using a fixed layout.
- **Simple API**: A single `show()` method is provided to display the dialog with all necessary configurations.
- **Callback-Driven**: Uses lambda functions (`onConfirm`, `onClose`) for easy handling of user actions.
- **Customizable Content**: Allows for custom text for the title, description, and both action buttons via method parameters.
- **Dismissibility Control**: Provides an option to control whether the dialog can be dismissed by tapping outside of it or pressing the back button.

---

## Kotlin Implementation

The primary way to use this component is via the `ModalityConfirmationPopUp` object, which provides a static `show()` method to create and display the dialog.

### Method Details

#### `show()`
Displays a confirmation alert dialog with specified content and action handlers.

```Kotlin
object ModalityConfirmationPopUp {
    fun show(
        context: Context,
        title: String,
        description: String,
        confirmButtonLabel: String,
        closeButtonLabel: String,
        isDismissible: Boolean = true,
        onConfirm: () -> Unit,
        onClose: () -> Unit
    )
}
```

#### Parameters
| Parameter            | Type         | Description                                                                                  |
| -------------------- | ------------ | -------------------------------------------------------------------------------------------- |
| `context`            | `Context`    | The context in which the dialog should be displayed.                                         |
| `title`              | `String`     | The title text displayed at the top of the dialog.                                           |
| `description`        | `String`     | The main message or descriptive text of the dialog.                                          |
| `confirmButtonLabel` | `String`     | The text for the primary (positive) action button.                                           |
| `closeButtonLabel`   | `String`     | The text for the secondary (negative) action button.                                         |
| `isDismissible`      | `Boolean`    | Sets whether the dialog can be dismissed by tapping the background or pressing the back key. |
| `onConfirm`          | `() -> Unit` | A lambda function that will be executed when the user clicks the confirmation button.        |
| `onClose`            | `() -> Unit` | A lambda function that will be executed when the user clicks the close button.               |

#### Example Usage
```Kotlin
// Inside a Fragment or Activity
private fun showEventModalityConfirmation() {
    ModalityConfirmationPopUp.show(
        context = requireContext(),
        title = "Dialog Title",
        description = "This is a description text for the modal. You can change anything here for your confirmation message.",
        confirmButtonLabel = "Positive",
        closeButtonLabel = "Negative",
        onConfirm = {
            // Handle the confirmation action
            Toast.makeText(context, "Confirmed", Toast.LENGTH_SHORT).show()
        },
        onClose = {
            // Handle the close/cancel action
            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    )
}

// Call the function when needed, for example, on a button click
binding.btnEventModalityConfirmation.setOnClickListener {
    showEventModalityConfirmation()
}
```

---
## Static View XML Implementation
While primarily intended as a pop-up dialog, the underlying `EventModalityConfirmation` view can also be embedded directly into your XML layouts as a static component. This is useful for scenarios where a confirmation UI needs to be a permanent part of a screen.

#### XML Usage
Add the component to your layout file. You can customize its content using the provided attributes.

```XML
<com.edts.components.modal.EventModalityConfirmation
    android:id="@+id/static_confirmation_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:modalTitle="Confirm Your Choice"
    app:modalDescription="This action cannot be undone. Please review your selection before proceeding."
    app:modalConfirmButtonLabel="I Understand"
    app:modalCloseButtonLabel="Go Back" />
```

#### Attributes
| Attribute                 | Format   | Description                                           |
| ------------------------- | -------- | ----------------------------------------------------- |
| `modalTitle`              | `String` | Sets the title of the component.                      |
| `modalDescription`        | `String` | Sets the description text.                            |
| `modalConfirmButtonLabel` | `String` | Sets the label for the primary confirmation button.   |
| `modalCloseButtonLabel`   | `String` | Sets the label for the secondary close/cancel button. |

---

## Handling Clicks
When using `EventModalityConfirmation` as a static view, you must implement the `EventModalityConfirmationDelegate` interface in your Fragment or Activity to handle button clicks.

```Kotlin
// In your Fragment or Activity class, implement the delegate
class MyFragment : Fragment(), EventModalityConfirmationDelegate {

    private lateinit var binding: MyFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the delegate on your static view instance
        binding.staticConfirmationView.delegate = this
    }

    // Override the required methods from the delegate
    override fun onConfirmClick(modality: EventModalityConfirmation) {
        // Handle confirm click for the static view
        Toast.makeText(context, "Static Confirm Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onCloseClick(modality: EventModalityConfirmation) {
        // Handle close click for the static view
        Toast.makeText(context, "Static Close Clicked", Toast.LENGTH_SHORT).show()
    }
}
```