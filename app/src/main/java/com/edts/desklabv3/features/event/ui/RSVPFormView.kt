package com.edts.desklabv3.features.event.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.input.field.InputField
import com.edts.components.input.field.InputFieldConfig
import com.edts.components.input.field.InputFieldType
import com.edts.components.status.badge.StatusBadge
import com.edts.components.toast.Toast
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.FragmentRsvpFormViewBinding
import com.edts.desklabv3.features.SpaceItemDecoration

class RSVPFormView : Fragment(), FooterDelegate {

    private lateinit var binding: FragmentRsvpFormViewBinding
    private lateinit var adapter: RSVPFormAdapter
    private val formConfigs = mutableListOf<InputFieldConfig>()

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

        setupRecyclerView()
        setupFormFields()
        setupFooter()
    }

    private fun setupRecyclerView() {
        adapter = RSVPFormAdapter()

        // Listen for response changes to update submit button state
        adapter.onResponseChange = { responses ->
            updateSubmitButtonState(responses)
        }

        binding.rvRsvpFormList.apply {
            adapter = this@RSVPFormView.adapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null // Optional: disable animations for better performance

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
                title = "Full Name",
                hint = "Enter your full name",
                isRequired = true
            ),
            InputFieldConfig(
                type = InputFieldType.TextInput,
                title = "Email Address",
                hint = "your.email@example.com",
                isRequired = true
            ),
            InputFieldConfig(
                type = InputFieldType.Dropdown,
                title = "Attendance Status",
                hint = "Select your attendance",
                isRequired = true,
                options = listOf("Attending", "Not Attending", "Maybe")
            ),
            InputFieldConfig(
                type = InputFieldType.TextArea,
                title = "Additional Notes",
                hint = "Any special requirements or notes...",
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
            setPrimaryButtonEnabled(false) // Disable initially until form is valid
        }
    }

    private fun updateSubmitButtonState(responses: Map<String, Any?>) {
        val isValid = validateForm(responses)
        binding.eventRsvpFooter.setPrimaryButtonEnabled(isValid)
    }

    private fun validateForm(responses: Map<String, Any?>): Boolean {
        // Get the form configs to check which fields are required
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

    // FooterDelegate implementation
    override fun onPrimaryButtonClicked(footerType: Footer.FooterType) {
        when (footerType) {
            Footer.FooterType.CALL_TO_ACTION -> onSubmitClicked()
            else -> {}
        }
    }

    override fun onRegisterClicked() {
        onSubmitClicked()
    }

    override fun onSecondaryButtonClicked(footerType: Footer.FooterType) {
        // Not used for CALL_TO_ACTION type
    }

    override fun onContinueClicked() {
        // Not used for CALL_TO_ACTION type
    }

    override fun onCancelClicked() {
        // Not used for CALL_TO_ACTION type
    }

    private fun onSubmitClicked() {
        val responses = adapter.getResponses()

        if (validateForm(responses)) {
            submitForm(responses)
        } else {
            showValidationErrors()
        }
    }

    private fun showValidationErrors() {
        // Highlight required fields that are empty
        for ((index, config) in formConfigs.withIndex()) {
            if (config.isRequired) {
                val fieldId = "field_$index"
                val value = adapter.getResponses()[fieldId]
                if (value == null || value.toString().isBlank()) {
                    // You might want to show error on the specific field
                    showFieldError(index, "This field is required")
                }
            }
        }
        Toast.error(requireContext(), "Please fill all required fields")
    }

    private fun showFieldError(position: Int, message: String) {
        // This is tricky with RecyclerView since views might be recycled
        // You might need to modify your adapter to handle error states
        val holder = binding.rvRsvpFormList.findViewHolderForAdapterPosition(position)
        if (holder is RSVPFormAdapter.ViewHolder) {
            holder.inputField.setError(message)
        }
    }

    private fun submitForm(responses: Map<String, Any?>) {
        // Show loading state
        binding.eventRsvpFooter.setPrimaryButtonEnabled(false)
        binding.eventRsvpFooter.setPrimaryButtonText("Submitting...")

        Log.d("RSVP", "Submitting form responses: $responses")
        handleSubmissionResult(true, "RSVP submitted successfully!")
    }

    private fun handleSubmissionResult(success: Boolean, message: String) {
        if (success) {
            showSuccess(message)
        } else {
            showError(message)
            // Reset footer to allow retry
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

        // Optional: Disable form fields after successful submission
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




//    private lateinit var binding: FragmentRsvpFormViewBinding
//    private lateinit var adapter: RSVPFormAdapter
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupRecyclerView()
//        setupFormFields()
//        setupSubmitButton()
//    }
//
//    private fun setupRecyclerView() {
//        adapter = RSVPFormAdapter()
//
//        adapter.onResponseChange = { responses ->
//            // Optional: Do something when any field changes
//            Log.d("Form", "Current responses: $responses")
//        }
//
//        binding.rvRsvpFormList.apply {
//            adapter = this@RSVPFormView.adapter
//            layoutManager = LinearLayoutManager(requireContext())
//        }
//    }
//
//    private fun setupFormFields() {
//        val formConfigs = listOf(
//            InputFieldConfig(
//                type = InputFieldType.TextInput,
//                title = "Full Name",
//                isRequired = true
//            ),
//            InputFieldConfig(
//                type = InputFieldType.TextInput,
//                title = "Email",
//                isRequired = true
//            ),
//            InputFieldConfig(
//                type = InputFieldType.Dropdown,
//                title = "How many guests?",
//                options = listOf("0", "1", "2", "3", "4+")
//            )
//        )
//
//        adapter.submitList(formConfigs)
//    }
//
//    private fun setupSubmitButton() {
//        binding.btnSubmit.setOnClickListener {
//            val responses = adapter.getResponses()
//            Log.d("RSVP", "Submitting: $responses")
//        }
//    }
}