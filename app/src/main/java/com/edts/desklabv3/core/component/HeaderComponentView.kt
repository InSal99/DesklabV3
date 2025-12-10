package com.edts.desklabv3.core.component

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edts.components.tab.Tab
import com.edts.components.tab.TabData
import com.edts.components.tab.TabItem
import com.edts.components.toast.Toast
import com.edts.desklabv3.core.util.InsetConfigurable
import com.edts.desklabv3.databinding.FragmentHeaderComponentViewBinding

class HeaderComponentView : Fragment(), InsetConfigurable {
    private var _binding: FragmentHeaderComponentViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeaderComponentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLibHeaderBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        binding.headerWithTab.tabView.setTabs(
            listOf(
                TabData("Item 1", "1", true, TabItem.TabState.ACTIVE),
                TabData("Item 2", "2", true, TabItem.TabState.INACTIVE),
                TabData("Item 3", "3", true, TabItem.TabState.INACTIVE),
                TabData("Item 4", "", false, TabItem.TabState.INACTIVE),
                TabData("Item 5", "", false, TabItem.TabState.INACTIVE),
                TabData("Item 6", "", false, TabItem.TabState.INACTIVE),
                TabData("Item 7", "", false, TabItem.TabState.INACTIVE),
                TabData("Item 8", "", false, TabItem.TabState.INACTIVE),
            ),
            selected = 0
        )

        binding.headerWithTab.tabView.setOnTabClickListener(
            object : Tab.OnTabClickListener {
                override fun onTabClick(position: Int, tabText: String) {
                    Toast.info(requireContext(), "Tab Item Click")
                }
            }
        )



    }

    override fun applyBottomInset(): Boolean = false

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}