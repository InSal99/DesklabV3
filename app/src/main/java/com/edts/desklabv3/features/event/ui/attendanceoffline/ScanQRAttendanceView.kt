package com.edts.desklabv3.features.event.ui.attendanceoffline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.desklabv3.databinding.FragmentScanQrAttendanceViewBinding

class ScanQRAttendanceView : Fragment() {
    private var _binding: FragmentScanQrAttendanceViewBinding? = null
    private val binding get() = _binding!!
    private var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrAttendanceViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        setupBackgroundClickListener()
    }

    private fun setupBackButton() {
        binding.ivBackBtnScanQRAttendance.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupBackgroundClickListener() {
        binding.ivBackgroundScanQRAttendance.setOnClickListener {
            showLoadingState()
        }
    }

    private fun showLoadingState() {
        loadingDialog = ModalityLoadingPopUp.show(
            context = requireContext(),
            title = "Tunggu sebentar ...",
            isCancelable = false
        )

        binding.root.postDelayed({
            loadingDialog?.dismiss()
            navigateToSuccessAttendance()
        }, 3000)
    }

    private fun navigateToSuccessAttendance() {
        val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
        parentFragmentManager.setFragmentResult("navigate_fragment", result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}