package com.edts.desklabv3.features.event.ui.rsvp

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.input.field.InputField
import com.edts.components.input.field.InputFieldConfig
import com.edts.components.input.field.InputFieldType
import com.edts.components.modal.ModalityConfirmationPopUp
import com.edts.components.modal.ModalityLoadingPopUp
import com.edts.components.status.badge.StatusBadge
import com.edts.components.toast.Toast
import com.edts.desklabv3.databinding.FragmentRsvpFormViewBinding
import com.edts.desklabv3.features.SpaceItemDecoration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RSVPFormView : Fragment(), FooterDelegate {

    private lateinit var binding: FragmentRsvpFormViewBinding
    private lateinit var adapter: RSVPFormAdapter
    private val formConfigs = mutableListOf<InputFieldConfig>()
    private var footerHeight = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRsvpFormViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        setupRecyclerView()
        setupFormFields()
        setupFooter()

        binding.rvRsvpFormList.clipToPadding = false
        binding.eventRsvpFooter.doOnLayout { footerHeight = it.height }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = if (imeVisible) {
                imeInsets.bottom - navBarInsets.bottom
            } else 0

            binding.rootScrollView.updatePadding(bottom = imeHeight)
            binding.eventRsvpFooter.translationY = -imeHeight.toFloat()

            insets
        }

    }

    private fun setupBackButton() {
        binding.ivRsvpBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        adapter = RSVPFormAdapter()

        adapter.onResponseChange = { responses ->
            updateSubmitButtonState(responses)
        }

        binding.rvRsvpFormList.apply {
            adapter = this@RSVPFormView.adapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null

            addItemDecoration(
                SpaceItemDecoration(
                    context = requireContext(),
                    spaceResId = com.edts.components.R.dimen.margin_24dp,
                    orientation = SpaceItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupFormFields() {
        val formConfigs = listOf(
            InputFieldConfig(
                type = InputFieldType.TextInput,
                title = "Username Mobile Legend",
                hint = "Masukan username kamu",
                isRequired = true
            ),
            InputFieldConfig(
                type = InputFieldType.NumberInput,
                title = "Jumlah Anggota Tim",
                hint = "Masukan jumlah anggota tim kamu",
                isRequired = true
            ),
            InputFieldConfig(
                type = InputFieldType.Dropdown,
                title = "Minuman",
                hint = "Pilih Minuman",
                isRequired = true,
                options = listOf("Teh", "Kopi", "Matcha", "Air Mineral"),
                description = "Peserta yang hadir akan mendapatkan minuman"
            ),
            InputFieldConfig(
                    type = InputFieldType.RadioGroup,
                    title = "Apakah kamu akan hadir langsung?",
                    options = listOf("Ya", "Tidak"),
                    isRequired = true
            ),
            InputFieldConfig(
                type = InputFieldType.CheckboxGroup,
                title = "Dari mana kamu mengetahui event ini?",
                options = listOf("Social Media", "Teman/Saudara", "Website EDTS", "Lainnya"),
                isRequired = true
            ),
            InputFieldConfig(
                type = InputFieldType.TextArea,
                title = "Alasan kamu mengikuti cara",
                hint = "Masukan alasan kamu di sini",
                minLines = 3,
                maxLines = 5,
                maxLength = 500,
                isRequired = false
            )
        )

        adapter.submitList(formConfigs)
    }

    private fun setupFooter() {
        binding.eventRsvpFooter.delegate = this
        binding.eventRsvpFooter.apply {
            setFooterType(Footer.FooterType.CALL_TO_ACTION)
            setPrimaryButtonText("Submit RSVP")
            setPrimaryButtonEnabled(true)
        }
    }

    private fun updateSubmitButtonState(responses: Map<String, Any?>) {
//        val isValid = validateForm(responses)
        binding.eventRsvpFooter.setPrimaryButtonEnabled(true)
    }

    private fun validateForm(responses: Map<String, Any?>): Boolean {
        for ((index, config) in formConfigs.withIndex()) {
            if (config.isRequired) {
                val fieldId = "field_$index"
                val value = responses[fieldId]
                if (value == null || value.toString().isBlank()) {
                    return false
                }
            }
        }
        return true
    }

    override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
        when (footerType) {
            Footer.FooterType.CALL_TO_ACTION -> onSubmitClicked()
            else -> {}
        }
    }

    override fun onRegisterClicked() {
        onSubmitClicked()
    }

    override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {}
    override fun onContinueClicked() {}
    override fun onCancelClicked() {}

    private var isSubmitting = false

    private fun onSubmitClicked() {
        if (isSubmitting) return
        isSubmitting = true

        val allValid = adapter.validateAllFieldsAndShowErrors()
        if (allValid) {
            val responses = adapter.getResponses()
            ModalityConfirmationPopUp.show(
                context = requireContext(),
                title = "Lanjutkan Pendaftaran?",
                description = "Dengan melanjutkan, Anda akan terdaftar sebagai peserta. Lanjutkan pendaftaran?",
                confirmButtonLabel = "Ya, Lanjutkan",
                closeButtonLabel = "Tidak",
                isDismissible = true,
                onConfirm = {
                    submitForm(responses)
                },
                onClose = {
                    isSubmitting = false
                }
            )
        } else {
            isSubmitting = false
        }
    }


    private fun showFieldError(position: Int, message: String) {
        val holder = binding.rvRsvpFormList.findViewHolderForAdapterPosition(position)
        if (holder is RSVPFormAdapter.ViewHolder) {
            holder.inputField.setError(message)
        }
    }

    private fun submitForm(responses: Map<String, Any?>) {
        val loadingDialog = ModalityLoadingPopUp.show(
            context = requireContext(),
            title = "Tunggu sebentar ..."
        )

        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)

            loadingDialog?.dismiss()

            requireActivity().supportFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            val result = bundleOf(
                "fragment_class" to "SuccessRegistrationView",
                "flow_type" to "RegisRSVP"
            )
            requireActivity().supportFragmentManager.setFragmentResult("navigate_fragment", result)
        }
    }


    private fun handleSubmissionResult(success: Boolean, message: String) {
        if (success) {
            showSuccess(message)
        } else {
            showError(message)
            resetFooter()
        }
    }

    private fun showSuccess(message: String) {
        binding.eventRsvpFooter.apply {
            setFooterType(Footer.FooterType.NO_ACTION)
            setTitleAndDescription(
                "RSVP Submitted!",
                "Thank you for confirming your attendance."
            )
            setStatusBadge("Confirmed", StatusBadge.ChipType.APPROVED)
        }

        Toast.success(requireContext(), message)

        disableFormFields()
    }

    private fun showError(message: String) {
        Toast.error(requireContext(), "Error: $message")
    }

    private fun resetFooter() {
        binding.eventRsvpFooter.apply {
            setFooterType(Footer.FooterType.CALL_TO_ACTION)
            setPrimaryButtonText("Submit RSVP")
            setPrimaryButtonEnabled(true)
        }
    }

    private fun disableFormFields() {
        for (i in 0 until binding.rvRsvpFormList.childCount) {
            val child = binding.rvRsvpFormList.getChildAt(i)
            if (child is InputField) {
                child.isEnabled = false
            }
        }
    }
}