package com.edts.desklabv3.features.leave.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.edts.desklabv3.core.util.InsetConfigurable
import com.edts.desklabv3.databinding.FragmentEmployeeLeaveHistoryViewBinding

class EmployeeLeaveHistoryView : Fragment(), InsetConfigurable {

    private var _binding: FragmentEmployeeLeaveHistoryViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeLeaveHistoryViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvHeader.delegate = object : com.edts.components.header.HeaderDelegate {
            override fun onLeftButtonClicked() {
                parentFragmentManager.popBackStack()
            }

            override fun onRightButtonClicked() {
                // TODO: Implement if needed
            }
        }
    }

    override fun applyBottomInset(): Boolean = false

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
