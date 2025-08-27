package com.example.desklabv3.features.event.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desklabv3.features.event.model.MyEvent
import com.example.components.R
import com.example.desklabv3.databinding.ActivityMyEventsBinding

class MyEventsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val eventList = createSampleEventData()

        // Create the adapter and pass a lambda to handle item clicks
        val eventAdapter = MyEventAdapter(eventList) { event ->
            // This code block will be executed when a card is clicked
            // You can navigate to a detail screen or show a dialog here
            Toast.makeText(this, "Clicked on: ${event.title}", Toast.LENGTH_SHORT).show()
        }

        // Setup the RecyclerView
        binding.rvMyEvent.apply {
            layoutManager = LinearLayoutManager(this@MyEventsActivity, LinearLayoutManager.VERTICAL, false)
            adapter = eventAdapter
        }
    }

    /**
     * Generates a list of sample events to populate the RecyclerView.
     */
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
            ),
            MyEvent(
                badgeText = "Tidak Hadir",
                badgeBackgroundColor = R.attr.colorBackgroundTertiary,
                badgeTextColor = R.attr.colorForegroundTertiary,
                date = "21",
                day = "Thu",
                month = "SEP",
                time = "13:00 - 14:00 WIB",
                title = "EDTS Town-Hall 2025: The Power of Change",
                eventType = "Hybrid Event"
            ),
            MyEvent(
                badgeText = "Tidak Hadir",
                badgeBackgroundColor = R.attr.colorBackgroundTertiary,
                badgeTextColor = R.attr.colorForegroundTertiary,
                date = "21",
                day = "Thu",
                month = "SEP",
                time = "13:00 - 14:00 WIB",
                title = "Insurance Socialization: Lippo Health Insurance",
                eventType = "Hybrid Event"
            ),
            MyEvent(
                badgeText = "Tidak Hadir",
                badgeBackgroundColor = R.attr.colorBackgroundTertiary,
                badgeTextColor = R.attr.colorForegroundTertiary,
                date = "21",
                day = "Thu",
                month = "SEP",
                time = "13:00 - 14:00 WIB",
                title = "Insurance Socialization: Lippo Health Insurance",
                eventType = "Hybrid Event"
            ),
            MyEvent(
                badgeText = "Tidak Hadir",
                badgeBackgroundColor = R.attr.colorBackgroundTertiary,
                badgeTextColor = R.attr.colorForegroundTertiary,
                date = "21",
                day = "Thu",
                month = "SEP",
                time = "13:00 - 14:00 WIB",
                title = "Insurance Socialization: Lippo Health Insurance",
                eventType = "Hybrid Event"
            )
        )
    }
}