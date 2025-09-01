package com.edts.desklabv3.features.event.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R
import com.edts.desklabv3.databinding.FragmentEventDetailBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventDetailView : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var timeLocationAdapter: EventTimeLocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup your RecyclerView and other logic here
        setupSpeakerRecyclerView()
        setupTimeLocationRecyclerView()
    }

    private fun setupSpeakerRecyclerView() {
        // Your RecyclerView setup code from earlier
        val speakerAdapter = EventSpeakerAdapter()
        binding.rvDetailEventSpeakersInfo.apply {
            adapter = speakerAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ItemSpacingDecoration(resources.getDimensionPixelSize(R.dimen.margin_8dp)))
        }

        // Load sample data
        val speakerList = listOf(
            R.drawable.avatar_placeholder to "John Doe",
            R.drawable.avatar_placeholder to "Jane Smith"
        )
        speakerAdapter.submitList(speakerList)
    }

    private fun setupTimeLocationRecyclerView() {
        timeLocationAdapter = EventTimeLocationAdapter()

        binding.rvDetailEventTimeLocation.apply {
            adapter = timeLocationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val startDateTime = "2023-12-25 19:00:00"
        val endDateTime = "2023-12-27 22:00:00"

        val timeLocationList = listOf(
            Triple(R.drawable.placeholder, "Tanggal", formatDateRange(startDateTime, endDateTime)),
            Triple(R.drawable.placeholder, "Waktu", formatTimeRange(startDateTime, endDateTime)),
            Triple(R.drawable.placeholder, "Lokasi Offline", "Grand Ballroom, Hotel Majestic"),
            Triple(R.drawable.placeholder, "Link Meeting", "123 Main Street, City Center")
        )

        timeLocationAdapter.submitList(timeLocationList)
    }

    private fun formatDateRange(startDateTime: String, endDateTime: String): String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val startDate = format.parse(startDateTime)!!
            val endDate = format.parse(endDateTime)!!

            val indonesianLocale = Locale("id", "ID")

            if (isSameDay(startDate, endDate)) {
                // Single day: "Senin, 25 Desember 2023"
                SimpleDateFormat("EEEE, dd MMMM yyyy", indonesianLocale).format(startDate)
            } else {
                if (isSameYear(startDate, endDate)) {
                    // Same year: "Senin, 25 Desember - Rabu, 27 Desember 2023"
                    val dateFormat = SimpleDateFormat("EEEE, dd MMMM", indonesianLocale)
                    "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)} ${SimpleDateFormat("yyyy", indonesianLocale).format(endDate)}"
                } else {
                    // Different years: "Senin, 25 Desember 2023 - Rabu, 27 Desember 2024"
                    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", indonesianLocale)
                    "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)}"
                }
            }
        } catch (e: Exception) {
            "Tanggal tidak valid"
        }
    }

    private fun isSameYear(date1: Date, date2: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date1
        val year1 = calendar.get(Calendar.YEAR)
        calendar.time = date2
        val year2 = calendar.get(Calendar.YEAR)
        return year1 == year2
    }

    private fun formatTimeRange(startDateTime: String, endDateTime: String): String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val startDate = format.parse(startDateTime)!!
            val endDate = format.parse(endDateTime)!!

            // Use Indonesian locale with 24-hour format (common in Indonesia)
            val indonesianLocale = Locale("id", "ID")
            val timeFormat = SimpleDateFormat("HH:mm", indonesianLocale) // "19:00 - 22:00"

            "${timeFormat.format(startDate)} - ${timeFormat.format(endDate)} WIB"
        } catch (e: Exception) {
            "Waktu tidak valid"
        }
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date1
        val day1 = calendar.get(Calendar.DAY_OF_YEAR)
        calendar.time = date2
        val day2 = calendar.get(Calendar.DAY_OF_YEAR)
        return day1 == day2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = spacing
        }
    }

}