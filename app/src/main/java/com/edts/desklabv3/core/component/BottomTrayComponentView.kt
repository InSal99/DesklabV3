package com.edts.desklabv3.core.component

import android.content.DialogInterface
import android.os.Bundle
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
                        Footer.FooterType.DUAL_BUTTON -> { }
                        else -> { }
                    }
                }

                override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
                    when (footerType) {
                        Footer.FooterType.DUAL_BUTTON -> {
                            bottomTray.dismiss()
                        }
                        else -> {}
                    }
                }

                override fun onRegisterClicked() { }

                override fun onContinueClicked() { }

                override fun onCancelClicked() {
                    bottomTray.dismiss()
                }
            }
        }

        bottomTray.delegate = object : BottomTrayDelegate {
            override fun onShow(dialogInterface: DialogInterface) { }

            override fun onDismiss(dialogInterface: DialogInterface) { }

            override fun onStateChanged(bottomSheet: View, newState: Int) { }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        }
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
                override fun onItemSelected(position: Int, data: Any?) { }
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