package com.edts.desklabv3.features.event.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.edts.components.tab.TabData
import com.edts.components.tab.TabItem
import com.edts.desklabv3.HeaderConfigurator
import com.edts.desklabv3.TabFragmentAdapter
import com.edts.desklabv3.core.util.setupWithViewPager2
import com.edts.desklabv3.databinding.FragmentEventMenuBinding
import com.edts.desklabv3.features.event.ui.eventlist.EventListAttendanceView
import com.edts.desklabv3.features.event.ui.eventlist.EventListDaftarRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationNoRSVPView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakEndView
import com.edts.desklabv3.features.event.ui.eventlist.EventListInvitationTolakStartView
import com.edts.desklabv3.features.event.ui.invitation.EventInvitationEmptyView
import com.edts.desklabv3.features.event.ui.invitation.EventInvitationFragmentNoRSVP
import com.edts.desklabv3.features.event.ui.invitation.EventInvitationFragmentTolakUndangan
import com.edts.desklabv3.features.event.ui.myevent.MyEventsEmptyView
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentAttendance
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentNoRSVP
import com.edts.desklabv3.features.event.ui.myevent.MyEventsFragmentRSVP

class EventMenuFragment : Fragment() {

    private var _binding: FragmentEventMenuBinding? = null
    private val binding get() = _binding!!
    private val pagerAdapters = mutableMapOf<String, TabFragmentAdapter>()

    private var headerConfigurator: HeaderConfigurator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerConfigurator?.configureHeader(
            title = "Event",
            showLeftButton = false
        )

        val flowType = arguments?.getString("flow_type", "") ?: ""
        val selectedTab: Int = arguments?.getInt("selected_tab", 0) ?: 0

        when (flowType) {
            "RegisRSVP" -> setupFlow1ViewPager(selectedTab)
            "RegisEndRSVP" -> setupFlow1EndViewPager(selectedTab)
            "Attendance" -> setupFlow2ViewPager(selectedTab)
            "AttendanceEnd" -> setupFlow2EndViewPager(selectedTab)
            "InvitationNoRSVP" -> setupFlow3ViewPager(selectedTab)
            "InvitationENDNoRSVP" -> setupFlow3EndViewPager(selectedTab)
            "TolakUndangan" -> setupFlow4ViewPager(selectedTab)
            "TolakUndanganEnd" -> setupFlow4EndViewPager(selectedTab)
            else -> Fragment()
        }

        Log.d("UI_CONFIG", "Event Menu Flow $flowType")
    }

    private fun setupFlow2EndViewPager(selectedPosition: Int = 0) {
        val fragments = listOf(
            EventListAttendanceView.newInstance(useEndList = true),
            MyEventsFragmentAttendance.newInstance(useEndList = true),
            EventInvitationEmptyView()
        )
        setupViewPager("flow2", fragments, selectedPosition)
    }

    private fun setupFlow3EndViewPager(selectedPosition: Int = 0) {
        val fragments = listOf(
            EventListInvitationNoRSVPView.newInstance(useEndList = true),
            MyEventsFragmentNoRSVP(),
            EventInvitationEmptyView()
        )
        setupViewPager("flow3", fragments, selectedPosition)
    }

    private fun setupFlow1EndViewPager(selectedPosition: Int = 0) {
        val fragments = listOf(
            EventListDaftarRSVPView.newInstance(useEndList = true),
            MyEventsFragmentRSVP(),
            EventInvitationEmptyView()
        )
        setupViewPager("flow1end", fragments, selectedPosition)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureTabs(selectedPosition: Int = 0) {
        val flowType = arguments?.getString("flow_type", "") ?: ""

        val tabDataList = listOf(
            TabData(
                text = "Daftar Event",
                badgeText = null,
                showBadge = false,
                state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
            ),
            TabData(
                text = "Event Saya",
                badgeText = null,
                showBadge = false,
                state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
            ),
            TabData(
                text = "Undangan",
                badgeText = if (flowType == "InvitationNoRSVP" || flowType == "TolakUndangan") "1" else null,
                showBadge = flowType == "InvitationNoRSVP" || flowType == "TolakUndangan",
                state = if (selectedPosition == 2) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
            )
        )

        binding.cvTabEventListDaftarRSVP.setTabs(tabDataList, selectedPosition)
    }

    private fun setupViewPager(
        flowKey: String,
        fragments: List<Fragment>,
        selectedPosition: Int = 0
    ) {
        configureTabs(selectedPosition)

//        val adapter = pagerAdapters.getOrPut(flowKey) {
//            TabFragmentAdapter(requireActivity(), fragments)
//        }

        val adapter = TabFragmentAdapter(this, fragments)

        binding.viewPager.post {
            if (binding.viewPager.adapter != adapter) {
                binding.viewPager.adapter = null
                binding.viewPager.adapter = adapter
                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
            }
            binding.viewPager.setCurrentItem(selectedPosition, false)
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    private fun setupFlow1ViewPager(selectedPosition: Int = 0) {
        val fragments = listOf(
            EventListDaftarRSVPView(),
            MyEventsEmptyView(),
            EventInvitationEmptyView()
        )
        setupViewPager("flow1", fragments, selectedPosition)
    }

    private fun setupFlow2ViewPager(selectedPosition: Int = 0) {
        val fragments = listOf(
            EventListInvitationNoRSVPView(),
            MyEventsFragmentRSVP(),
            EventInvitationFragmentNoRSVP()
//            EventListAttendanceView(),
//            MyEventsFragmentAttendance(),
//            EventInvitationEmptyView()
        )
        setupViewPager("flow2", fragments, selectedPosition)
    }

    private fun setupFlow3ViewPager(selectedPosition: Int = 0) {
        val fragments = listOf(
            EventListAttendanceView(),
            MyEventsFragmentAttendance(),
            EventInvitationEmptyView()
//            EventListInvitationNoRSVPView(),
//            MyEventsFragmentNoRSVP(),
//            EventInvitationFragmentNoRSVP()
        )
        setupViewPager("flow3", fragments, selectedPosition)
    }

    private fun setupFlow4ViewPager(selectedPosition: Int = 0) {
        val fragments = listOf(
            EventListInvitationTolakStartView(),
            MyEventsEmptyView(),
            EventInvitationFragmentTolakUndangan()
        )
        setupViewPager("flow4", fragments, selectedPosition)
    }

    private fun setupFlow4EndViewPager(selectedPosition: Int = 0) {
        val fragments = listOf(
            EventListInvitationTolakEndView(),
            MyEventsEmptyView(),
            EventInvitationEmptyView()
        )
        setupViewPager("flow4end", fragments, selectedPosition)
    }
}










//class EventMenuFragment: Fragment() {
//
//    private var _binding: FragmentEventMenuBinding? = null
//    private val binding get() = _binding!!
//
//    private var flow1PagerAdapter: TabFragmentAdapter? = null
//    private var flow2PagerAdapter: TabFragmentAdapter? = null
//    private var flow3PagerAdapter: TabFragmentAdapter? = null
//    private var flow4PagerAdapter: TabFragmentAdapter? = null
//    private var headerConfigurator: HeaderConfigurator? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentEventMenuBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        headerConfigurator?.configureHeader(
//            title = "Event",
//            showLeftButton = false
//        )
//
////        setupFlow1ViewPager()
////        setupFlow2ViewPager()
////        setupFlow3ViewPager()
////        setupFlow4ViewPager()
//
//        val flowType = arguments?.getString("flow_type", "") ?: ""
//        val selectedTab: Int = arguments?.getInt("selected_tab", 0) ?: 0
//
//        when (flowType) {
//            "RegisRSVP" -> setupFlow1ViewPager(selectedTab)
//            "InvitationNoRSVP" -> setupFlow2ViewPager(selectedTab)
//            "Attendance" -> setupFlow3ViewPager(selectedTab)
//            "TolakUndangan" -> setupFlow4ViewPager(selectedTab)
//            else -> setupFlow1ViewPager(selectedTab)
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is HeaderConfigurator) {
//            headerConfigurator = context
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        headerConfigurator = null
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun configureFlow1Tabs(selectedPosition: Int = 0) {
//        val tabDataList = mutableListOf<TabData>()
//
//        tabDataList.add(
//            TabData(
//            text = "Daftar Event",
//            badgeText = null,
//            showBadge = false,
//            state = if (selectedPosition == 0) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
//        )
//        )
//
//        tabDataList.add(
//            TabData(
//            text = "Event Saya",
//            badgeText = null,
//            showBadge = false,
//            state = if (selectedPosition == 1) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
//        )
//        )
//
//        tabDataList.add(
//            TabData(
//            text = "Undangan",
//            badgeText = null,
//            showBadge = false,
//            state = if (selectedPosition == 2) TabItem.TabState.ACTIVE else TabItem.TabState.INACTIVE
//        )
//        )
//
//        binding.cvTabEventListDaftarRSVP.setTabs(tabDataList, selectedPosition)
//    }
//
//    private fun setupFlow1ViewPager(selectedPosition: Int = 0) {
//        configureFlow1Tabs(selectedPosition)
//
//        if (flow1PagerAdapter == null) {
//            val fragments = listOf(
//                EventListDaftarRSVPView(),
//                MyEventsFragmentRSVP(),
//                EventInvitationEmptyView()
//            )
//            flow1PagerAdapter = TabFragmentAdapter(requireActivity(), fragments)
//        }
//
//        if (binding.viewPager.adapter != flow1PagerAdapter) {
//            binding.viewPager.adapter = null
//            binding.viewPager.adapter = flow1PagerAdapter
//            binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
//        }
//        binding.viewPager.setCurrentItem(selectedPosition, false)
//    }
//
//    private fun setupFlow2ViewPager(selectedPosition: Int = 0) {
//        if (flow2PagerAdapter == null) {
//            val fragments = listOf(
//                EventListAttendanceView(),    // Tab 0: Daftar Event
//                MyEventsFragmentAttendance(),        // Tab 1: Event Saya (empty)
//                EventInvitationEmptyView()         // Tab 2: Undangan (empty)
//            )
//            flow2PagerAdapter = TabFragmentAdapter(requireActivity(), fragments)
//        }
//
//        binding.viewPager.post {
//            if (binding.viewPager.adapter != flow2PagerAdapter) {
//                binding.viewPager.adapter = null
//                binding.viewPager.adapter = flow2PagerAdapter
//                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
//            }
//            binding.viewPager.setCurrentItem(selectedPosition, false)
//            binding.viewPager.visibility = View.VISIBLE
//        }
//        binding.viewPager.setCurrentItem(selectedPosition, false)
//    }
//
//    // Flow 3: HomeInvitationNoRSVPView → EventListInvitationNoRSVPView + MyEventsFragmentNoRSVP + EventInvitationFragmentNoRSVP
//    private fun setupFlow3ViewPager(selectedPosition: Int = 0) {
//        if (flow3PagerAdapter == null) {
//            val fragments = listOf(
//                EventListInvitationNoRSVPView(),  // Tab 0: Daftar Event
//                MyEventsFragmentNoRSVP(),         // Tab 1: Event Saya
//                EventInvitationFragmentNoRSVP()   // Tab 2: Undangan
//            )
//            flow3PagerAdapter = TabFragmentAdapter(requireActivity(), fragments)
//        }
//
//        binding.viewPager.post {
//            if (binding.viewPager.adapter != flow3PagerAdapter) {
//                binding.viewPager.adapter = null
//                binding.viewPager.adapter = flow3PagerAdapter
//                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
//            }
//            binding.viewPager.setCurrentItem(selectedPosition, false)
//            binding.viewPager.visibility = View.VISIBLE
//        }
//        binding.viewPager.setCurrentItem(selectedPosition, false)
//    }
//
//    // Flow 4: HomeInvitationTolakView → EventListInvitationTolakStartView + Empty Event Saya + EventInvitationFragmentTolakUndangan
//    private fun setupFlow4ViewPager(selectedPosition: Int = 0) {
//
//        if (flow4PagerAdapter == null) {
//            val fragments = listOf(
//                EventListInvitationTolakStartView(),    // Tab 0: Daftar Event
//                MyEventsEmptyView(),                   // Tab 1: Event Saya (empty)
//                EventInvitationFragmentTolakUndangan()  // Tab 2: Undangan
//            )
//            flow4PagerAdapter = TabFragmentAdapter(requireActivity(), fragments)
//        }
//
//        binding.viewPager.post {
//            if (binding.viewPager.adapter != flow4PagerAdapter) {
//                binding.viewPager.adapter = null
//                binding.viewPager.adapter = flow4PagerAdapter
//                binding.cvTabEventListDaftarRSVP.setupWithViewPager2(binding.viewPager)
//            }
//            binding.viewPager.setCurrentItem(selectedPosition, false)
//            binding.viewPager.visibility = View.VISIBLE
//        }
//        binding.viewPager.setCurrentItem(selectedPosition, false)
//    }
//
//}