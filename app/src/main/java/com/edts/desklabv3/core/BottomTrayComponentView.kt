package com.edts.desklabv3.core

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.radiobutton.RadioGroup
import com.edts.components.radiobutton.RadioGroupDelegate
import com.edts.components.tray.BottomTray
import com.edts.components.tray.BottomTrayDelegate
import com.edts.desklabv3.databinding.FragmentBottomTrayComponentViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomTrayComponentView : Fragment() {

    private var _binding: FragmentBottomTrayComponentViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomTrayComponentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLibBottomTrayBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        binding.btnLibBottomTrayShow.setOnClickListener {
            showBottomTray()
        }
    }

    private fun showBottomTray() {
        val bottomTray = BottomTray.newInstance(
            title = "Bottom Tray",
            showDragHandle = true,
            showFooter = true,
            hasShadow = true,
            hasStroke = true
        )

        val contentView = createTrayContentView()
        bottomTray.setTrayContentView(contentView)

        bottomTray.configureFooter { footer ->
            footer.setFooterType(Footer.FooterType.DUAL_BUTTON)
            footer.setPrimaryButtonText("Lanjutkan")
            footer.setSecondaryButtonText("Batal")
            footer.setDualButtonDescription("Total Amount", "Rp 500.000", "Rp 250.000 x 2")
            footer.setDescriptionVisibility(true)
            footer.setPrimaryButtonEnabled(true)
            footer.setSecondaryButtonEnabled(true)

            footer.delegate = object : FooterDelegate {
                override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
                    when (footerType) {
                        Footer.FooterType.DUAL_BUTTON -> {
                            Log.d("BottomTray", "Lanjutkan button clicked")
                        }
                        else -> {}
                    }
                }

                override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
                    when (footerType) {
                        Footer.FooterType.DUAL_BUTTON -> {
                            Log.d("BottomTray", "Batal button clicked")
                            bottomTray.dismiss()
                        }
                        else -> {}
                    }
                }

                override fun onRegisterClicked() {
                    Log.d("BottomTray", "Lanjutkan clicked")
                }

                override fun onContinueClicked() {
                    Log.d("BottomTray", "Continue clicked")
                }

                override fun onCancelClicked() {
                    Log.d("BottomTray", "Batal clicked")
                    bottomTray.dismiss()
                }
            }
        }

        bottomTray.delegate = object : BottomTrayDelegate {
            override fun onShow(dialogInterface: DialogInterface) {
                Log.d("BottomTray", "Tray shown")
            }

            override fun onDismiss(dialogInterface: DialogInterface) {
                Log.d("BottomTray", "Tray dismissed")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> Log.d("BottomTray", "Expanded")
                    BottomSheetBehavior.STATE_COLLAPSED -> Log.d("BottomTray", "Collapsed")
                    BottomSheetBehavior.STATE_HIDDEN -> Log.d("BottomTray", "Hidden")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("BottomTray", "Slide offset: $slideOffset")
            }
        }

        bottomTray.snapPoints = intArrayOf(300, 600)
        bottomTray.show(parentFragmentManager, "BottomTrayTag")
    }

    private fun createTrayContentView(): View {
        val mainContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16.dpToPx(), 0, 16.dpToPx(), 0)
        }

        val options = listOf("Option A", "Option B", "Option C", "Option D")

        val radioGroup = RadioGroup(requireContext()).apply {
            buttonSpacing = 8.dpToPx()
            setData(options) { it -> it }

            setOnItemSelectedListener(object : RadioGroupDelegate {
                override fun onItemSelected(position: Int, data: Any?) {
                    Log.d("BottomTray", "Selected position: $position, data: $data")
                }
            })

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        mainContainer.addView(radioGroup)
        return mainContainer
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}





//class BottomTrayComponentView : Fragment() {
//
//    private var _binding: FragmentBottomTrayComponentViewBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentBottomTrayComponentViewBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.btnLibBottomTrayBack.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
//
//        setupButtonClickListener()
//    }
//
//    private fun setupButtonClickListener() {
//        binding.btnLibBottomTrayShow.setOnClickListener {
//            showBottomTray()
//        }
//    }
//
//    private fun showBottomTray() {
//        // Create and configure your bottom tray
//        val bottomTray = BottomTray.newInstance(
//            title = "Bottom Tray",
//            showDragHandle = true,
//            showFooter = true,
//            hasShadow = true,
//            hasStroke = true
//        )
//
//        // Set up the content view for the bottom tray
//        val contentView = createTrayContentView()
//        bottomTray.setContentView(contentView)
//
//        // Configure footer
//        bottomTray.configureFooter { footer ->
//            // Set the footer type based on your needs
//            footer.setFooterType(Footer.FooterType.DUAL_BUTTON)
//
//            // Set button texts
//            footer.setPrimaryButtonText("Batal")
//            footer.setSecondaryButtonText("Lanjutkan")
//
//            // Set description visibility
//            footer.setDescriptionVisibility(true)
//
//            // Set button enabled states
//            footer.setPrimaryButtonEnabled(true)
//            footer.setSecondaryButtonEnabled(true)
//
//            // Set footer delegate
//            footer.delegate = object : FooterDelegate {
//                override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
//                    when (footerType) {
//                        Footer.FooterType.DUAL_BUTTON -> {
//                            // Handle continue action
//                            Log.d("BottomTray", "Lanjutkan button clicked")
//                            // Perform your continue logic here
//                        }
//                        Footer.FooterType.CALL_TO_ACTION,
//                        Footer.FooterType.CALL_TO_ACTION_DETAIL -> {
//                            // Handle register action
//                            Log.d("BottomTray", "Lanjutkan button clicked")
//                        }
//                        Footer.FooterType.NO_ACTION -> {}
//                    }
//                }
//
//                override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
//                    when (footerType) {
//                        Footer.FooterType.DUAL_BUTTON -> {
//                            // Handle cancel action
//                            Log.d("BottomTray", "Batalkan button clicked")
//                            bottomTray.dismiss()
//                        }
//                        else -> {}
//                    }
//                }
//
//                override fun onRegisterClicked() {
//                    // Handle register specifically
//                    Log.d("BottomTray", "Lanjutkan clicked")
//                }
//
//                override fun onContinueClicked() {
//                    // Handle continue specifically
//                    Log.d("BottomTray", "Continue clicked")
//                    // Perform your continue logic here
//                }
//
//                override fun onCancelClicked() {
//                    // Handle cancel specifically
//                    Log.d("BottomTray", "Batalkan clicked")
//                    bottomTray.dismiss()
//                }
//            }
//        }
//
//        // Set up bottom tray delegate
//        bottomTray.delegate = object : BottomTrayDelegate {
//            override fun onShow(dialogInterface: DialogInterface) {
//                Log.d("BottomTray", "Tray shown")
//            }
//
//            override fun onDismiss(dialogInterface: DialogInterface) {
//                Log.d("BottomTray", "Tray dismissed")
//            }
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_EXPANDED -> Log.d("BottomTray", "Expanded")
//                    BottomSheetBehavior.STATE_COLLAPSED -> Log.d("BottomTray", "Collapsed")
//                    BottomSheetBehavior.STATE_HIDDEN -> Log.d("BottomTray", "Hidden")
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                Log.d("BottomTray", "Slide offset: $slideOffset")
//            }
//        }
//
//        // Set snap points if needed (optional)
//        bottomTray.snapPoints = intArrayOf(300, 600) // Heights in pixels
//
//        // Show the bottom tray
//        bottomTray.show(parentFragmentManager, "BottomTrayTag")
//    }
//
//    private fun createTrayContentView(): View {
//        val mainContainer = LinearLayout(requireContext()).apply {
//            orientation = LinearLayout.VERTICAL
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            setPadding(16.dpToPx(), 0, 16.dpToPx(), 0)
//        }
//
//        // Create your data list
//        val options = listOf("Option A", "Option B", "Option C", "Option D")
//
//        // Create and configure your custom RadioGroup
//        val radioGroup = RadioGroup(requireContext()).apply {
//            // Set the button spacing to 8dp
//            buttonSpacing = 8.dpToPx() // This should set the margin between buttons
//
//            // Set data with display provider
//            setData(options) { it -> it }
//
//            setOnItemSelectedListener(object : RadioGroupDelegate {
//                override fun onItemSelected(position: Int, data: Any?) {
//                    Log.d("BottomTray", "Selected position: $position, data: $data")
//                }
//            })
//
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//        }
//
//        mainContainer.addView(radioGroup)
//
//        return mainContainer
//    }
//
//    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
//}