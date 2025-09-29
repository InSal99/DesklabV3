package com.edts.desklabv3.features.event.ui.attendanceoffline

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.edts.components.toast.Toast
import com.edts.desklabv3.databinding.FragmentAssetQrCodeReaderBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.desklabv3.core.util.InsetConfigurable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AssetQRCodeFragment : Fragment(), InsetConfigurable {

    private var _binding: FragmentAssetQrCodeReaderBinding? = null
    private val binding get() = _binding!!
    private var isInitialized = false
    private var isInitializing = false
    private var isProcessingQR = false

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                initializeCamera()
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
        setupScannerOverlay()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onResume() {
        super.onResume()
        checkAndInitializeCamera()
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
        isInitialized = false
        isInitializing = false
        isProcessingQR = false
        _binding = null
    }

    private fun checkAndInitializeCamera() {
        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        when {
            hasPermission && !isInitialized && !isInitializing -> {
                initializeCamera()
            }
            hasPermission && isInitialized -> {
                binding.qrCodeReaderView.startCamera()
            }
            !hasPermission -> {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun applyBottomInset(): Boolean = false

    private fun initializeCamera() {
        if (isInitialized || isInitializing) return

        isInitializing = true

        binding.qrCodeReaderView.post {
            binding.qrCodeReaderView.apply {
                setQRDecodingEnabled(true)
                setAutofocusInterval(2000L)
                setBackCamera()
            }

            setupScannerOverlay()
            setQrCodeReaderListener()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.qrCodeReaderView.startCamera()
                isInitialized = true
                isInitializing = false

                Handler(Looper.getMainLooper()).postDelayed({
                    forceViewRefresh()
                }, 100)
            }, 300)
        }
    }

    private fun forceViewRefresh() {
        binding.qrCodeReaderView.apply {
            visibility = View.INVISIBLE
            post {
                visibility = View.VISIBLE
                requestLayout()
                invalidate()
                binding.root.requestLayout()
                binding.root.invalidate()
            }
        }
    }

    private fun setup() {
        binding.ivScanQRBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.ivScanQRFlash.setOnClickListener {
            processQRFound("some text")
            val flashIcon = binding.qrCodeReaderView.switchFlash()
            binding.ivScanQRFlash.setImageResource(flashIcon)
        }
    }

    private fun setupScannerOverlay() {
        if (binding.qrCodeReaderView.width == 0 || binding.qrCodeReaderView.height == 0) {
            binding.qrCodeReaderView.post { setupScannerOverlay() }
            return
        }

        val screenWidth = binding.qrCodeReaderView.width
        val screenHeight = binding.qrCodeReaderView.height
        val scannerSize = minOf(screenWidth, screenHeight) * 0.6f

        val marginTop = ((screenHeight - scannerSize) / 2.0).toInt()
        val marginLeft = ((screenWidth - scannerSize) / 2.0).toInt()

        binding.vUp.layoutParams.height = marginTop
        binding.vUp.requestLayout()

        binding.vBottom.layoutParams.height = marginTop
        binding.vBottom.requestLayout()

        binding.vLeft.layoutParams.width = marginLeft
        binding.vLeft.requestLayout()

        binding.vRight.layoutParams.width = marginLeft
        binding.vRight.requestLayout()

        binding.ivScanQRFlash.post {
            setupScannerAnimation()
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
            val rectF = RectF(
                binding.flScan.x,
                binding.flScan.y,
                binding.flScan.x + binding.flScan.width,
                binding.flScan.y + binding.flScan.height
            )

            var found = false
            for (pointF in points) {
                if (!rectF.contains(pointF.x, pointF.y)) {
                    found = true
                    break
                }
            }

            if (found) {
                processQRFound(text)
            }
        }
    }

//    private fun processQRFound(text: String) {
//        Log.d("QRFragment", "QR Found")
//        val dialog = ModalityLoadingPopUp.show(requireContext(), "Tunggu Sebentar...")
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            dialog?.dismiss()
//            val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
//            parentFragmentManager.setFragmentResult("navigate_fragment", result)
//            qrCodeResultListener?.invoke(text)
//        }, 3000)
//
//        if (!text.startsWith("desklab://")) {
//            Log.d("QRFragment", "Link format is compatible")
//        }
//    }

    private fun processQRFound(text: String) {
        if (isProcessingQR) return
        isProcessingQR = true

        Log.d("QRFragment", "QR Found")

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val dialog = ModalityLoadingPopUp.show(requireContext(), "Tunggu Sebentar...")

            delay(3000)

            if (isAdded && !isDetached && view != null) {
                try {
                    // Ensure dialog dismiss happens on main thread
                    dialog?.dismiss()

                    // Wait for dismissal to complete
                    delay(300)

                    // Navigate only after dialog is dismissed
                    if (isAdded && view != null) {
                        val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
                        parentFragmentManager.setFragmentResult("navigate_fragment", result)
                        qrCodeResultListener?.invoke(text)
                    }
                } catch (e: Exception) {
                    Log.e("QRFragment", "Error in processQRFound", e)
                    dialog?.dismiss()
                }
            } else {
                dialog?.dismiss()
            }

            isProcessingQR = false
        }

        if (!text.startsWith("desklab://")) {
            Log.d("QRFragment", "Link format is compatible")
        }
    }

//    private fun processQRFound(text: String) {
//        if (isProcessingQR) return
//        isProcessingQR = true
//
//        Log.d("QRFragment", "QR Found")
//
//        val dialog = ModalityLoadingPopUp.show(requireContext(), "Tunggu Sebentar...")
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            if (isAdded && view != null) {
//                dialog?.setOnDismissListener {
//                    if (isAdded && view != null) {
//                        val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
//                        parentFragmentManager.setFragmentResult("navigate_fragment", result)
//                        qrCodeResultListener?.invoke(text)
//                    }
//                    isProcessingQR = false
//                }
//                dialog?.dismiss()
//            } else {
//                dialog?.dismiss()
//                isProcessingQR = false
//            }
//        }, 3000)
//    }

//    private fun processQRFound(text: String) {
//        if (isProcessingQR) return
//        isProcessingQR = true
//
//        Log.d("QRFragment", "QR Found")
//
//        val dialog = ModalityLoadingPopUp.show(requireContext(), "Tunggu Sebentar...")
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            try {
//                if (isAdded && view != null) {
//                    dialog?.dismiss()
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        if (isAdded && view != null) {
//                            val result = bundleOf("fragment_class" to "SuccessAttendanceOfflineView")
//                            parentFragmentManager.setFragmentResult("navigate_fragment", result)
//                            qrCodeResultListener?.invoke(text)
//                        }
//                        isProcessingQR = false
//                    }, 200) // Small delay after dismissal
//                } else {
//                    dialog?.dismiss()
//                    isProcessingQR = false
//                }
//            } catch (e: Exception) {
//                Log.e("QRFragment", "Error in processQRFound", e)
//                dialog?.dismiss()
//                isProcessingQR = false
//            }
//        }, 3000)
//
//        if (!text.startsWith("desklab://")) {
//            Log.d("QRFragment", "Link format is compatible")
//        }
//    }
}