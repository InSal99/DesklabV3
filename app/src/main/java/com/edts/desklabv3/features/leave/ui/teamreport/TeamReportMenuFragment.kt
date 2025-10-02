package com.edts.desklabv3.features.leave.ui.teamreport

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.edts.components.tab.Tab
import com.edts.components.tab.TabData
import com.edts.components.tab.TabItem
import com.edts.desklabv3.HeaderConfigurator
import com.edts.desklabv3.TabFragmentAdapter
import com.edts.desklabv3.core.util.InsetConfigurable
import com.edts.desklabv3.core.util.setupWithViewPager2
import com.edts.desklabv3.databinding.FragmentTeamReportMenuBinding

class TeamReportMenuFragment : Fragment(), InsetConfigurable {

    private var _binding: FragmentTeamReportMenuBinding? = null
    private val binding get() = _binding!!

    private var teamReportPagerAdapter: TabFragmentAdapter? = null
    private var headerConfigurator: HeaderConfigurator? = null
    private var currentTeamReportTab: Int = 0
    private var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamReportMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.isSaveEnabled = false

        headerConfigurator?.configureHeader(
            title = "Event",
            subtitle = "Kelola Event Anda",
            showLeftButton = true,
            onLeftClick = { requireActivity().onBackPressedDispatcher.onBackPressed() }
        )
        setupTeamReportViewPager(currentTeamReportTab)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HeaderConfigurator) {
            headerConfigurator = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        headerConfigurator = null
    }

    override fun onResume() {
        super.onResume()
        binding.viewPager.post {
            val position = binding.viewPager.currentItem
            currentTeamReportTab = position
            configureTeamReportTabs(position)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pageChangeCallback?.let { binding.viewPager.unregisterOnPageChangeCallback(it) }
        binding.viewPager.adapter = null
        teamReportPagerAdapter = null
        _binding = null
    }

    override fun applyBottomInset(): Boolean = false

    private fun setupTeamReportViewPager(selectedPosition: Int = 0) {
        currentTeamReportTab = selectedPosition

        if (teamReportPagerAdapter == null) {
            val fragments = listOf(
                TeamReportActivityView(),
                TeamReportLeaveView()
            )
            teamReportPagerAdapter = TabFragmentAdapter(requireActivity(), fragments)
            binding.viewPager.adapter = teamReportPagerAdapter
            binding.cvTabTeamReportActivity.setupWithViewPager2(binding.viewPager)

//            binding.viewPager.isUserInputEnabled = false

            pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentTeamReportTab = position
                    configureTeamReportTabs(position)
                }
            }

            binding.viewPager.registerOnPageChangeCallback(pageChangeCallback!!)
        }

        binding.cvTabTeamReportActivity.setOnTabClickListener(object : Tab.OnTabClickListener {
            override fun onTabClick(position: Int, tabText: String) {
                binding.viewPager.setCurrentItem(position, false)
            }
        })

        binding.viewPager.post {
            binding.viewPager.setCurrentItem(selectedPosition, false)
            binding.viewPager.visibility = View.VISIBLE
        }
    }


    private fun configureTeamReportTabs(selectedPosition: Int = 0) {
        Log.d("UI_CONFIG", "Configuring team report tabs: selectedPosition=$selectedPosition")

        val tabDataList = mutableListOf<TabData>()

        tabDataList.add(TabData(
            text = "Aktivitas",
            badgeText = "",
            showBadge = false,
            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        tabDataList.add(TabData(
            text = "Cuti",
            badgeText = "",
            showBadge = false,
            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
        ))

        binding.cvTabTeamReportActivity.setTabs(tabDataList, selectedPosition)
    }

}