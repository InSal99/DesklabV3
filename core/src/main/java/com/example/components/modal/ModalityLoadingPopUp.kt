package com.example.components.modal

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.edts.components.databinding.CustomEventModalityLoadingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A stateless utility object to create and display a loading dialog.
 *
 * This utility provides a simple way to show a consistent loading modal across the application.
 * It follows the same pattern as `ModalityConfirmationPopUp`, where the `show` method
 * creates and returns a dialog instance for the caller to manage.
 *
 * ### Usage Example:
 * ```kotlin
 * // Create and show the dialog
 * val loadingDialog = ModalityLoadingDialog.show(context, "Loading your data...")
 *
 * // ... after your operation is complete ...
 *
 * // Hide the dialog
 * loadingDialog?.dismiss()
 * ```
 */
object ModalityLoadingPopUp {

    /**
     * Creates and shows the loading dialog.
     *
     * @param context The context to use for the dialog.
     * @param title The text to display in the loading dialog.
     * @param isCancelable Whether the dialog can be dismissed by tapping outside or
     * pressing the back button. Defaults to `false`.
     * @return The created `AlertDialog` instance, or `null` if an error occurred.
     */
    fun show(
        context: Context,
        title: String,
        isCancelable: Boolean = false
    ): AlertDialog? {
        return try {

            val binding = CustomEventModalityLoadingBinding.inflate(LayoutInflater.from(context))
            binding.tvModalTitle.text = title

            val builder = MaterialAlertDialogBuilder(context)
            builder.setView(binding.root)

            val dialog = builder.create().apply {
                setCancelable(isCancelable)
            }

            dialog.show()
            dialog

        } catch (e: Exception) {
            Log.e("ModalityLoadingDialog", "Error showing dialog", e)
            null
        }
    }
}
