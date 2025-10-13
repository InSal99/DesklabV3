package com.edts.desklabv3.core

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.desklabv3.databinding.FragmentEventModalityLoadingComponentBinding

class EventModalityLoadingComponentFragment : Fragment() {
    private var _binding: FragmentEventModalityLoadingComponentBinding? = null
    private val binding get() = _binding!!
    private var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventModalityLoadingComponentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEventModalityLoading.setOnClickListener {
            showEventModalityLoading()
        }

        binding.btnLibEventModalityLoadingBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showEventModalityLoading() {
        loadingDialog = ModalityLoadingPopUp.show(
            context = requireContext(),
            title = "Loading Title ...",
            isCancelable = false
        )

        Handler(Looper.getMainLooper()).postDelayed({
            loadingDialog?.dismiss()
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}