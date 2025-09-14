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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.edts.components.toast.Toast
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentAssetQrCodeReaderBinding
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import com.edts.components.modal.ModalityLoadingPopUp

class AssetQRCodeFragment : Fragment() {

    private var _binding: FragmentAssetQrCodeReaderBinding? = null
    private val binding get() = _binding!!

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                restartCameraAfterPermission()
            } else {
                Toast.error(requireContext(), "Camera permission is required")
            }
        }

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

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    override fun onStop() {
        binding.qrCodeReaderView.stopCamera()
        super.onStop()
    }

    override fun onPause() {
        binding.qrCodeReaderView.stopCamera()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCameraWithDelay()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun restartCameraAfterPermission() {
        binding.qrCodeReaderView.post {
            try {
                binding.qrCodeReaderView.stopCamera()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.qrCodeReaderView.startCamera()
                    Log.d("QRFragment", "Camera restarted after permission grant")
                }, 200)
            } catch (e: Exception) {
                Log.e("QRFragment", "Failed to restart camera: ${e.message}")
            }
        }
    }

    private fun startCameraWithDelay() {
        binding.qrCodeReaderView.post {
            try {
                binding.qrCodeReaderView.startCamera()
                Log.d("QRFragment", "Camera started successfully")
            } catch (e: Exception) {
                Log.e("QRFragment", "Failed to start camera: ${e.message}")
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
        binding.ivScanQRBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ivScanQRFlash.setOnClickListener {
            val flashIcon = binding.qrCodeReaderView.switchFlash()
            binding.ivScanQRFlash.setImageResource(flashIcon)
        }

        setQrCodeReaderListener()

        binding.qrCodeReaderView.post {
            setupScannerOverlay()
        }

        binding.qrCodeReaderView.setBackCamera()
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

            binding.ivScanQRFlash.post {
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
                        val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
                        parentFragmentManager.setFragmentResult("navigate_fragment", result)
                        qrCodeResultListener?.invoke(text)
                    }, 3000)
                    hideScanner()

                    if (text.startsWith("desklab://")) {
                        Log.d("QRFragment", "QR Code link match expected format")
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
//    private val requestCameraPermission =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                binding.qrCodeReaderView.startCamera()
//            } else {
//                Toast.error(requireContext(), "Camera permission is required")
//            }
//        }
//
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
//        checkCameraPermission()
//    }
//
//    override fun onStop() {
//        binding.qrCodeReaderView.stopCamera()
//        super.onStop()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            binding.qrCodeReaderView.startCamera()
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun checkCameraPermission() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            startCameraWithDelay()
//        } else {
//            requestCameraPermission.launch(Manifest.permission.CAMERA)
//        }
//    }
//
//    private fun startCameraWithDelay() {
//        binding.qrCodeReaderView.post {
//            try {
//                binding.qrCodeReaderView.startCamera()
//                Log.d("QRFragment", "Camera started successfully")
//            } catch (e: Exception) {
//                Log.e("QRFragment", "Failed to start camera: ${e.message}")
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
//        binding.ivScanQRBack.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
//
//        binding.ivScanQRFlash.setOnClickListener {
//            val flashIcon = binding.qrCodeReaderView.switchFlash()
//            binding.ivScanQRFlash.setImageResource(flashIcon)
//        }
//
//        setQrCodeReaderListener()
//
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
//            binding.ivScanQRFlash.post {
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
//                    val dialog = ModalityLoadingPopUp.show(requireContext(), "Tunggu Sebentar...")
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        dialog?.dismiss()
//                        val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
//                        parentFragmentManager.setFragmentResult("navigate_fragment", result)
//                        qrCodeResultListener?.invoke(text)
//                    }, 3000)
//                    hideScanner()
//
//                    if (text.startsWith("desklab://")) {
//                        Log.d("QRFragment", "QR Code link match expected format")
//                    } else {
//                        Log.d("QRFragment", "QR Code doesn't match expected format")
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