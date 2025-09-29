package com.edts.desklabv3.core.component

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edts.desklabv3.core.util.InsetConfigurable
import com.edts.desklabv3.databinding.FragmentEventCardComponentViewBinding

class EventCardComponentView : Fragment(), InsetConfigurable {
    private var _binding: FragmentEventCardComponentViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventCardComponentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLibEventCardBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun applyBottomInset(): Boolean = false

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}