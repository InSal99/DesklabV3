package com.edts.components.modal

import android.content.Context
import android.util.Log
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
            val binding = EventModalityConfirmationBinding.inflate(LayoutInflater.from(context))
            val builder = MaterialAlertDialogBuilder(context, R.style.Theme_App_Dialog_Confirmation)

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
            Log.e("ModalityConfirmation", "Error showing dialog", e)
        }
    }
}