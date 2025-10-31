package com.edts.components.modal

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import com.edts.components.R
import com.edts.components.databinding.EventModalityConfirmationBinding
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
            val themedContext = ContextThemeWrapper(context, R.style.Theme_Desklab_Kit)
            val binding = EventModalityConfirmationBinding.inflate(LayoutInflater.from(themedContext))
            val builder = MaterialAlertDialogBuilder(themedContext)

            builder.setView(binding.root)
            val dialog = builder.create()

            binding.tvModalityTitle.text = title
            binding.tvModalityDescription.text = description
            binding.btnModalityConfirm.text = confirmButtonLabel
            binding.btnModalityClose.text = closeButtonLabel

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
        }
    }
}