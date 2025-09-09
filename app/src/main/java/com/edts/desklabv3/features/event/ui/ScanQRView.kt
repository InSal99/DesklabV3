package com.edts.desklabv3.features.event.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.edts.components.modal.EventModalityLoading
import com.edts.components.toast.Toast
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentScanQrViewBinding
import android.os.Handler
import android.os.Looper
import android.view.Gravity

class ScanQRView : Fragment() {

    private var _binding: FragmentScanQrViewBinding? = null
    private val binding get() = _binding!!
    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.isClickable = true
        view.setOnTouchListener { v, event -> true }

        setupBackButton()
        setupScannerClickListener()
    }

    private fun setupBackButton() {
        binding.ivScanQRBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupScannerClickListener() {
        binding.ivScanQRScanner.setOnClickListener {
            showLoadingModal()
        }
    }

    private fun showLoadingModal() {
        hideLoadingModal()

        try {
            val loadingModal = EventModalityLoading(requireContext()).apply {
                setTitle("Tunggu sebentar ...")
            }

            val container = FrameLayout(requireContext()).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                addView(loadingModal)
            }

            loadingDialog = Dialog(requireContext()).apply {
                setContentView(container)

                window?.apply {
                    setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setDimAmount(0.5f)
                    setGravity(Gravity.CENTER)
                }

                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }

            loadingDialog?.show()
            startScanningProcess()

        } catch (e: Exception) {
            Log.e("ScanQRView", "Error showing loading modal", e)
            Toast.error(requireContext(), "Gagal memulai pemindaian")
        }
    }

    private fun startScanningProcess() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoadingModal()
            handleScanSuccess()
        }, 3000)
    }

    private fun handleScanSuccess() {
        Toast.success(requireContext(), "QR Code berhasil dipindai!")
//        parentFragmentManager.popBackStack()
    }

    private fun handleScanFailure() {
        Toast.error(requireContext(), "Gagal memindai QR Code")
    }

    private fun hideLoadingModal() {
        try {
            loadingDialog?.dismiss()
            loadingDialog = null
        } catch (e: Exception) {
            Log.e("ScanQRView", "Error hiding loading modal", e)
        }
    }

    override fun onPause() {
        super.onPause()
        hideLoadingModal()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoadingModal()
        _binding = null
    }
}