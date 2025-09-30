# Modality Loading

## Overview
The `ModalityLoadingPopUp` is a utility object designed to display a modal loading dialog. This component is essential for providing feedback to the user during long-running, asynchronous operations such as network requests or data processing. It blocks interaction with the underlying UI, ensuring the user waits for the task to complete.

The dialog consists of a circular progress indicator and a customizable title, clearly communicating that a process is underway.

## Key Features
- **Blocks UI Interaction**: Prevents user interaction with the app while a background task is running.
- **Clear Visual Feedback**: Uses a `CircularProgressIndicator` and a text label to inform the user of an active process.
- **Simple API**: A single static method, `ModalityLoadingPopUp.show()`, makes it extremely easy to implement.
- **Programmatic Control**: The `show()` method returns the `AlertDialog` instance, allowing you to dismiss the dialog programmatically when the task is finished.
- **Customizable Title**: The loading message can be customized to provide context for the operation being performed.
- **Cancelable Action**: Provides an option to control whether the user can dismiss the dialog using the back button or by tapping outside.

---
## Kotlin Implementation
The primary method for displaying the loading dialog is through the `ModalityLoadingPopUp` object.

### Method Details
#### `show()`
Inflates and displays a modal loading dialog, returning the created `AlertDialog` instance for later control.

```Kotlin
object ModalityLoadingPopUp {
    fun show(
        context: Context,
        title: String,
        isCancelable: Boolean = false
    ): AlertDialog?
}
```

### Parameters
| Parameter      | Type      | Description                                          |
| -------------- | --------- | ---------------------------------------------------- |
| `context`      | `Context` | The context in which the dialog should be displayed. |
| `title`        | `String`  | The title text displayed at the top of the dialog.   |
| `isCancelable` | `Boolean` | The main message or descriptive text of the dialog.  |
> [!note]
> The `show()` function returns the created `AlertDialog?` instance. This is crucial for dismissing the dialog once your background operation is complete. You should store this reference to call `.dismiss()` on it later.

### Example Usage
It's common to show the dialog before starting a background task (like a network call) and dismiss it in a `finally` block or when the task completes.

```Kotlin
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.launch

// ... inside a Fragment or Activity

// Keep a reference to the dialog
private var loadingDialog: AlertDialog? = null

private fun performSomeTask() {
    // Show the dialog and store the instance
    loadingDialog = ModalityLoadingPopUp.show(
        context = requireContext(),
        title = "Fetching data...",
        isCancelable = false
    )

    // Launch a coroutine for a background task
    lifecycleScope.launch {
        try {
            // Simulate network delay
            delay(3000) 
            // Data fetching logic here ...
        } finally {
            // Ensure the dialog is dismissed even if an error occurs
            loadingDialog?.dismiss()
        }
    }
}

// Call the function from a button click or another trigger
binding.myButton.setOnClickListener {
    performSomeTask()
}
```
