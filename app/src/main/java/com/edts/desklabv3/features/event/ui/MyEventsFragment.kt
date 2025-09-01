package com.edts.desklabv3.features.event.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edts.components.R
import com.edts.desklabv3.databinding.FragmentMyEventsBinding
import com.edts.desklabv3.features.event.model.MyEvent
import com.example.desklabv3.features.SpaceItemDecoration
import com.google.android.material.transition.MaterialSharedAxis

class MyEventsFragment : Fragment() {

    private var _binding: FragmentMyEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMyEventRecyclerView()
    }

    private fun setupMyEventRecyclerView() {
        val eventList = createSampleEventData()

        val eventAdapter = MyEventAdapter(eventList) { event ->
            Toast.makeText(requireContext(), "Clicked on: ${event.title}", Toast.LENGTH_SHORT).show()
        }

        val itemDecoration = SpaceItemDecoration(
            requireContext(),
            R.dimen.margin_8dp,
            SpaceItemDecoration.VERTICAL
        )

        binding.rvMyEvent.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(itemDecoration)
            adapter = eventAdapter
        }
    }

    private fun createSampleEventData(): List<MyEvent> {
        return listOf(
            MyEvent(
                badgeText = "Berlangsung",
                badgeBackgroundColor = R.attr.colorBackgroundAttentionIntense,
                badgeTextColor = R.attr.colorForegroundPrimaryInverse,
                date = "12",
                day = "Wed",
                month = "JUL",
                time = "18:00 - 20:00 WIB",
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                eventType = "Online Event"
            ),
            MyEvent(
                badgeText = "Terdaftar",
                badgeBackgroundColor = R.attr.colorBackgroundSuccessIntense,
                badgeTextColor = R.attr.colorForegroundPrimaryInverse,
                date = "15",
                day = "Sat",
                month = "JUL",
                time = "10:00 - 12:00 WIB",
                title = "IT Security Awareness: Stay Ahead of Threats, Stay Secure",
                eventType = "Online Event"
            ),
            MyEvent(
                badgeText = "Hadir",
                badgeBackgroundColor = R.attr.colorBackgroundTertiary,
                badgeTextColor = R.attr.colorForegroundTertiary,
                date = "05",
                day = "Mon",
                month = "AUG",
                time = "09:00 - 17:00 WIB",
                title = "Game Night with EDTS: Mobile Legend Online Tournament 2025",
                eventType = "Offline Event"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}