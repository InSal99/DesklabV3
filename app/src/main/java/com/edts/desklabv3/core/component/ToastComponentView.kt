package com.edts.desklabv3.core.component

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edts.components.toast.Toast
import com.edts.desklabv3.databinding.FragmentToastComponentViewBinding

class ToastComponentView : Fragment() {
    private var _binding: FragmentToastComponentViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToastComponentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLibToastBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnLibToastShow.setOnClickListener {
            showToastAnimation()
        }
    }

    private fun showToastAnimation() {
        Toast.info(requireContext(), "This is toast!")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}