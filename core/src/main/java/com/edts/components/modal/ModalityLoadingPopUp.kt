package com.edts.components.modal

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.edts.components.R
import com.edts.components.databinding.EventModalityLoadingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ModalityLoadingPopUp {
    fun show(
        context: Context,
        title: String,
        isCancelable: Boolean = false
    ): AlertDialog? {
        return try {
            val binding = EventModalityLoadingBinding.inflate(LayoutInflater.from(context))
            val builder = MaterialAlertDialogBuilder(context, R.style.Theme_App_Dialog_Confirmation)

            builder.setView(binding.root)
            binding.tvModalTitle.text = title


            val dialog = builder.create().apply {
                setCancelable(isCancelable)
            }

            dialog.show()
            dialog

        } catch (e: Exception) {
            null
        }
    }
}