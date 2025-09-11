package com.edts.desklabv3.features

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.edts.components.toast.Toast
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentAssetQrCodeReaderBinding
import android.util.Log
import androidx.core.os.bundleOf
import com.edts.components.modal.ModalityLoadingPopUp

class AssetQRCodeFragment : Fragment() {

    private var _binding: FragmentAssetQrCodeReaderBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

    // Callback to send result back to parent
    var qrCodeResultListener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssetQrCodeReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onStart() {
        super.onStart()
        // Check camera permission before starting camera
        checkCameraPermission()
    }

    override fun onStop() {
        binding.qrCodeReaderView.stopCamera()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, start camera
            startCameraWithDelay()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraWithDelay()
                } else {
                    // Handle permission denied
                    Toast.error(requireContext(), "Camera permission is required")
                }
            }
        }
    }

    // Helper function to start camera with delay and error handling
    private fun startCameraWithDelay() {
        binding.qrCodeReaderView.post {
            try {
                binding.qrCodeReaderView.startCamera()
                Log.d("QRFragment", "Camera started successfully")
            } catch (e: Exception) {
                Log.e("QRFragment", "Failed to start camera: ${e.message}")
                // Try to restart camera after a delay
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        binding.qrCodeReaderView.startCamera()
                    } catch (e2: Exception) {
                        Log.e("QRFragment", "Second attempt failed: ${e2.message}")
                    }
                }, 1000)
            }
        }
    }

    private fun setup() {
        // back button → pop fragment
        binding.flBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // flash toggle
        binding.flFlash.setOnClickListener {
            val flashIcon = binding.qrCodeReaderView.switchFlash()
            binding.ivFlash.setImageResource(flashIcon)
        }

        setQrCodeReaderListener()

        // scanner overlay & animation
        binding.qrCodeReaderView.post {
            setupScannerOverlay()
        }
    }

    private fun setupScannerOverlay() {
        try {
            val marginTop = (binding.qrCodeReaderView.height -
                    resources.getDimensionPixelSize(R.dimen.qr_code_scanner_height)) / 2.0
            val marginLeft = (binding.qrCodeReaderView.width -
                    resources.getDimensionPixelSize(R.dimen.qr_code_scanner_width)) / 2.0

            binding.vUp.layoutParams.height = marginTop.toInt()
            binding.vUp.requestLayout()

            binding.vBottom.layoutParams.height = marginTop.toInt()
            binding.vBottom.requestLayout()

            binding.vLeft.layoutParams.width = marginLeft.toInt()
            binding.vLeft.requestLayout()

            binding.vRight.layoutParams.width = marginLeft.toInt()
            binding.vRight.requestLayout()

            binding.flFlash.post {
                setupScannerAnimation()
            }
        } catch (e: Exception) {
            Log.e("QRFragment", "Error setting up scanner overlay: ${e.message}")
        }
    }

    private fun setupScannerAnimation() {
        binding.vScan.isVisible = true

        val animator = ObjectAnimator.ofFloat(
            binding.vScan,
            "translationY",
            0f,
            binding.flScan.height.toFloat()
        )
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 2000
        animator.start()
    }

//    private fun setQrCodeReaderListener() {
//        binding.qrCodeReaderView.setOnQRCodeReadListener { text, points ->
//            try {
//                val rectF = RectF(
//                    binding.flScan.x,
//                    binding.flScan.y,
//                    binding.flScan.x + binding.flScan.width,
//                    binding.flScan.y + binding.flScan.height
//                )
//
//                var found = true
//                for (pointF in points) {
//                    if (!rectF.contains(pointF.x, pointF.y)) {
//                        found = false
//                        break
//                    }
//                }
//
//                if (found) {
//                    Log.d("QRFragment", "QR Code detected: $text")
//                    hideScanner()
//                    if (text.startsWith("desklab://")) {
//                        qrCodeResultListener?.invoke(text) // send to parent
//                    } else {
//                        Log.d("QRFragment", "QR Code doesn't match expected format")
//                        // Show scanner again after delay if QR doesn't match
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            showScanner()
//                        }, 2000)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("QRFragment", "Error processing QR code: ${e.message}")
//            }
//        }
//    }

    private fun setQrCodeReaderListener() {
        binding.qrCodeReaderView.setOnQRCodeReadListener { text, points ->
            try {
                val rectF = RectF(
                    binding.flScan.x,
                    binding.flScan.y,
                    binding.flScan.x + binding.flScan.width,
                    binding.flScan.y + binding.flScan.height
                )

                var found = true
                for (pointF in points) {
                    if (!rectF.contains(pointF.x, pointF.y)) {
                        found = false
                        break
                    }
                }

                if (found) {
                    Log.d("QRFragment", "QR Code detected: $text")
                    val dialog = ModalityLoadingPopUp.show(requireContext(), "Tunggu Sebentar...")

                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog?.dismiss()

                        // ✅ Navigate to SuccessAttendanceOfflineView
                        val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
                        parentFragmentManager.setFragmentResult("navigate_fragment", result)

                        // If you still want to send result back to parent:
                        qrCodeResultListener?.invoke(text)

                    }, 3000) // 3 seconds
                    hideScanner()

                    if (text.startsWith("desklab://")) {
                        // ✅ Show loading popup
//                        val dialog = ModalityLoadingPopUp.show(requireContext(), "Tunggu Sebentar...")
//
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            dialog?.dismiss()
//
//                            // ✅ Navigate to SuccessAttendanceOfflineView
//                            val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
//                            parentFragmentManager.setFragmentResult("navigate_fragment", result)
//
//                            // If you still want to send result back to parent:
//                            qrCodeResultListener?.invoke(text)
//
//                        }, 3000) // 3 seconds
                    } else {
                        Log.d("QRFragment", "QR Code doesn't match expected format")
                        Handler(Looper.getMainLooper()).postDelayed({
                            showScanner()
                        }, 2000)
                    }
                }
            } catch (e: Exception) {
                Log.e("QRFragment", "Error processing QR code: ${e.message}")
            }
        }
    }


    private fun showScanner() {
        binding.clScanner.isVisible = true
        Handler(Looper.getMainLooper()).postDelayed({
            setQrCodeReaderListener()
        }, 1000)
    }

    private fun hideScanner() {
        binding.qrCodeReaderView.setOnQRCodeReadListener(null)
        binding.clScanner.isVisible = false
    }

    private fun restartCamera() {
        Handler(Looper.getMainLooper()).post {
            binding.qrCodeReaderView.stopCamera()
            startCameraWithDelay()
        }
    }
}



//class AssetQRCodeFragment : Fragment() {
//
//    private var _binding: FragmentAssetQrCodeReaderBinding? = null
//    private val binding get() = _binding!!
//
//    companion object {
//        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
//    }
//
//    // Callback to send result back to parent
//    var qrCodeResultListener: ((String) -> Unit)? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentAssetQrCodeReaderBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setup()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        // Check camera permission before starting camera
//        checkCameraPermission()
//    }
//
//    override fun onStop() {
//        binding.qrCodeReaderView.stopCamera()
//        super.onStop()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun checkCameraPermission() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.CAMERA),
//                CAMERA_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            // Permission already granted, start camera
//            startCameraWithDelay()
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        when (requestCode) {
//            CAMERA_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startCameraWithDelay()
//                } else {
//                    // Handle permission denied
//                    Toast.error(requireContext(), "Camera permission is required")
//                }
//            }
//        }
//    }
//
//    // Helper function to start camera with delay and error handling
//    private fun startCameraWithDelay() {
//        binding.qrCodeReaderView.post {
//            try {
//                binding.qrCodeReaderView.startCamera()
//                Log.d("QRFragment", "Camera started successfully")
//            } catch (e: Exception) {
//                Log.e("QRFragment", "Failed to start camera: ${e.message}")
//                // Try to restart camera after a delay
//                Handler(Looper.getMainLooper()).postDelayed({
//                    try {
//                        binding.qrCodeReaderView.startCamera()
//                    } catch (e2: Exception) {
//                        Log.e("QRFragment", "Second attempt failed: ${e2.message}")
//                    }
//                }, 1000)
//            }
//        }
//    }
//
//    private fun setup() {
//        // back button → pop fragment
//        binding.flBack.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
//
//        // flash toggle
//        binding.flFlash.setOnClickListener {
//            val flashIcon = binding.qrCodeReaderView.switchFlash()
//            binding.ivFlash.setImageResource(flashIcon)
//        }
//
//        setQrCodeReaderListener()
//
//        // scanner overlay & animation
//        binding.qrCodeReaderView.post {
//            setupScannerOverlay()
//        }
//    }
//
//    private fun setupScannerOverlay() {
//        try {
//            val marginTop = (binding.qrCodeReaderView.height -
//                    resources.getDimensionPixelSize(R.dimen.qr_code_scanner_height)) / 2.0
//            val marginLeft = (binding.qrCodeReaderView.width -
//                    resources.getDimensionPixelSize(R.dimen.qr_code_scanner_width)) / 2.0
//
//            binding.vUp.layoutParams.height = marginTop.toInt()
//            binding.vUp.requestLayout()
//
//            binding.vBottom.layoutParams.height = marginTop.toInt()
//            binding.vBottom.requestLayout()
//
//            binding.vLeft.layoutParams.width = marginLeft.toInt()
//            binding.vLeft.requestLayout()
//
//            binding.vRight.layoutParams.width = marginLeft.toInt()
//            binding.vRight.requestLayout()
//
//            binding.flFlash.post {
//                setupScannerAnimation()
//            }
//        } catch (e: Exception) {
//            Log.e("QRFragment", "Error setting up scanner overlay: ${e.message}")
//        }
//    }
//
//    private fun setupScannerAnimation() {
//        binding.vScan.isVisible = true
//
//        val animator = ObjectAnimator.ofFloat(
//            binding.vScan,
//            "translationY",
//            0f,
//            binding.flScan.height.toFloat()
//        )
//        animator.repeatMode = ValueAnimator.REVERSE
//        animator.repeatCount = ValueAnimator.INFINITE
//        animator.interpolator = AccelerateDecelerateInterpolator()
//        animator.duration = 2000
//        animator.start()
//    }
//
//    private fun setQrCodeReaderListener() {
//        binding.qrCodeReaderView.setOnQRCodeReadListener { text, points ->
//            try {
//                val rectF = RectF(
//                    binding.flScan.x,
//                    binding.flScan.y,
//                    binding.flScan.x + binding.flScan.width,
//                    binding.flScan.y + binding.flScan.height
//                )
//
//                var found = true
//                for (pointF in points) {
//                    if (!rectF.contains(pointF.x, pointF.y)) {
//                        found = false
//                        break
//                    }
//                }
//
//                if (found) {
//                    Log.d("QRFragment", "QR Code detected: $text")
//                    hideScanner()
//                    if (text.startsWith("desklab://")) {
//                        qrCodeResultListener?.invoke(text) // send to parent
//                    } else {
//                        Log.d("QRFragment", "QR Code doesn't match expected format")
//                        // Show scanner again after delay if QR doesn't match
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            showScanner()
//                        }, 2000)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("QRFragment", "Error processing QR code: ${e.message}")
//            }
//        }
//    }
//
//    private fun showScanner() {
//        binding.clScanner.isVisible = true
//        Handler(Looper.getMainLooper()).postDelayed({
//            setQrCodeReaderListener()
//        }, 1000)
//    }
//
//    private fun hideScanner() {
//        binding.qrCodeReaderView.setOnQRCodeReadListener(null)
//        binding.clScanner.isVisible = false
//    }
//
//    private fun restartCamera() {
//        Handler(Looper.getMainLooper()).post {
//            binding.qrCodeReaderView.stopCamera()
//            startCameraWithDelay()
//        }
//    }
//}
