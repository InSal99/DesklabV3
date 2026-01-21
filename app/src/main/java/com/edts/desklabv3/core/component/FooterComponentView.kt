package com.edts.desklabv3.core.component

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.desklabv3.databinding.FragmentFooterComponentViewBinding

class FooterComponentView : Fragment(), FooterDelegate {
    private var _binding: FragmentFooterComponentViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFooterComponentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLibFooterBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupFooterSimple()
        setupFooterDualButton()
    }

    private fun setupFooterSimple() {
        binding.footerSimple.apply {
            delegate = this@FooterComponentView

            setFooterType(Footer.FooterType.CALL_TO_ACTION)
            setPrimaryButtonText("Daftar Sekarang")

            setDualButtonDescription(
                title = "Total Pembayaran",
                supportText1 = "Rp 120.000",
                supportText2 = ""
            )

            setTitleVisibility(false)
            setHasCTASupportText2(false)
            setHasCTASupportText1(false)

            setPrimaryButtonEnabled(true)
            setStroke(true)
        }
    }

    private fun setupFooterDualButton() {
        binding.footerDualButton.apply {
            delegate = this@FooterComponentView

            setFooterType(Footer.FooterType.DUAL_BUTTON)

            setPrimaryButtonText("Lanjutkan")
            setSecondaryButtonText("Batal")

            setDualButtonDescription(
                title = "Metode Pembayaran",
                supportText1 = "Transfer Bank",
                supportText2 = "BCA"
            )

            setTitleVisibility(false)
            setHasDualButtonSupportText2(false)
            setHasDualButtonSupportText1(false)

            setPrimaryButtonEnabled(true)
            setSecondaryButtonEnabled(true)
            setStroke(true)
        }
    }

    override fun onPrimaryButtonClicked(type: Footer.FooterType) {}
    override fun onSecondaryButtonClicked(type: Footer.FooterType) {}
    override fun onRegisterClicked() {}
    override fun onContinueClicked() {}
    override fun onCancelClicked() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}