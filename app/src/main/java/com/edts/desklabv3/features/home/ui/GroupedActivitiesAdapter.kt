package com.edts.desklabv3.features.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edts.desklabv3.R
import com.edts.desklabv3.databinding.ItemDateGroupBinding
import com.edts.desklabv3.features.home.model.ActivityItem
import com.edts.desklabv3.features.home.model.DateGroupItem
import com.edts.desklabv3.features.SpaceItemDecoration
import java.text.SimpleDateFormat
import java.util.*

class GroupedActivitiesAdapter(
    private var dateGroups: List<DateGroupItem>,
    private val onActivityClick: (ActivityItem) -> Unit = {}
) : RecyclerView.Adapter<GroupedActivitiesAdapter.DateGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateGroupViewHolder {
        val binding = ItemDateGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DateGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateGroupViewHolder, position: Int) {
        holder.bind(dateGroups[position])
    }

    override fun getItemCount(): Int = dateGroups.size

    fun updateDateGroups(newDateGroups: List<DateGroupItem>) {
        dateGroups = newDateGroups
        notifyDataSetChanged()
    }

    inner class DateGroupViewHolder(private val binding: ItemDateGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        private var activityDecoration: RecyclerView.ItemDecoration? = null

        fun bind(dateGroup: DateGroupItem) {
            binding.tvDate.text = dateGroup.dayOfMonth
            binding.tvMonth.text = dateGroup.monthName

            val activityAdapter = ActivityAdapter(dateGroup.activities, onActivityClick)

            binding.rvActivities.apply {
                adapter = activityAdapter
                layoutManager = LinearLayoutManager(context)
                isNestedScrollingEnabled = false

                activityDecoration?.let { removeItemDecoration(it) }

                activityDecoration = SpaceItemDecoration(
                    context = context,
                    spaceResId = R.dimen.activity_item_spacing,
                    orientation = SpaceItemDecoration.VERTICAL
                )
                addItemDecoration(activityDecoration!!)
            }
        }
    }

    companion object {
        fun groupActivitiesByDate(activities: List<ActivityItem>): List<DateGroupItem> {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
            val monthFormat = SimpleDateFormat("MMM", Locale("id", "ID")) // Indonesian month names

            return activities
                .groupBy { it.date }
                .map { (dateString, activitiesForDate) ->
                    try {
                        val date = dateFormat.parse(dateString)
                        val dayOfMonth = dayFormat.format(date)
                        val monthName = monthFormat.format(date)

                        DateGroupItem(
                            date = dateString,
                            dayOfMonth = dayOfMonth,
                            monthName = monthName,
                            activities = activitiesForDate
                        )
                    } catch (e: Exception) {
                        DateGroupItem(
                            date = dateString,
                            dayOfMonth = dateString.substring(8, 10), // Assuming yyyy-MM-dd format
                            monthName = getMonthName(dateString.substring(5, 7)),
                            activities = activitiesForDate
                        )
                    }
                }
                .sortedBy { it.date }
        }

        private fun getMonthName(monthNumber: String): String {
            return when (monthNumber) {
                "01" -> "Jan"
                "02" -> "Feb"
                "03" -> "Mar"
                "04" -> "Apr"
                "05" -> "Mei"
                "06" -> "Jun"
                "07" -> "Jul"
                "08" -> "Agu"
                "09" -> "Sep"
                "10" -> "Okt"
                "11" -> "Nov"
                "12" -> "Des"
                else -> "Jan"
            }
        }
    }
}