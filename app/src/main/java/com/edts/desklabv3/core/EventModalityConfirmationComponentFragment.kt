package com.edts.desklabv3.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.edts.components.modal.ModalityConfirmationPopUp
import com.edts.desklabv3.databinding.FragmentEventModalityConfirmationComponentBinding

class EventModalityConfirmationComponentFragment : Fragment() {

    private var _binding: FragmentEventModalityConfirmationComponentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventModalityConfirmationComponentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEventModalityConfirmation.setOnClickListener {
            showEventModalityConfirmation()
        }

        binding.btnLibEventModalityConfirmationBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showEventModalityConfirmation() {
        ModalityConfirmationPopUp.show(
            context = requireContext(),
            title = "Dialog Title",
            description = "This is a description text for the modal. You can change anything here for your confirmation message.",
            confirmButtonLabel = "Positive",
            closeButtonLabel = "Negative",
            onConfirm = {
                Toast.makeText(context, "Confirmed", Toast.LENGTH_SHORT).show()
            },
            onClose = {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}