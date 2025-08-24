package com.edts.components.modal

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.edts.components.databinding.CustomEventModalityConfirmationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
    ) {
        try {
            val binding = CustomEventModalityConfirmationBinding.inflate(LayoutInflater.from(context))

            // The builder no longer needs a theme passed to it.
            val builder = MaterialAlertDialogBuilder(context)

            builder.setView(binding.root)
            val dialog = builder.create()

            // Set the text for the views
            binding.tvModalityTitle.text = title
            binding.tvModalityDescription.text = description
            binding.btnModalityConfirm.text = confirmButtonLabel
            binding.btnModalityClose.text = closeButtonLabel

            // Set click listeners
            binding.btnModalityConfirm.setOnClickListener {
                onConfirm()
                dialog.dismiss()
            }
            binding.btnModalityClose.setOnClickListener {
                onClose()
                dialog.dismiss()
            }

            dialog.setCancelable(isDismissible)
            dialog.show()

        } catch (e: Exception) {
            Log.e("ModalityConfirmation", "Error showing dialog", e)
        }
    }
}