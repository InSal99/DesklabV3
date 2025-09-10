package com.edts.desklabv3.features.home.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.R
import com.edts.desklabv3.databinding.ItemActivityBinding
import com.edts.desklabv3.features.home.model.ActivityItem
import com.edts.desklabv3.features.home.model.ActivityType

class ActivityAdapter(
    private var activities: List<ActivityItem>,
    private val onItemClick: (ActivityItem) -> Unit = {}
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size

    inner class ActivityViewHolder(private val binding: ItemActivityBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: ActivityItem) {
            binding.cardActivity.apply {
                titleText = activity.title
                descText = activity.desc

                showRightSlot = activity.haveSummary

                when (activity.type) {
                    ActivityType.Event -> {
                        indicatorColor = resolveColorAttribute(R.attr.colorStrokeUtilityEventIntense)
                    }
                    ActivityType.Cuti -> {
                        indicatorColor = resolveColorAttribute(R.attr.colorStrokeUtilityCutiIntense)
                    }
                    ActivityType.KerjaKhusus -> {
                        indicatorColor = resolveColorAttribute(R.attr.colorStrokeUtilityKerjaKhususIntense)
                    }
                    ActivityType.UlangTahun -> {
                        indicatorColor = resolveColorAttribute(R.attr.colorStrokeUtilityUlangTahunIntense)
                    }
                    ActivityType.Libur -> {
                        indicatorColor = resolveColorAttribute(R.attr.colorStrokeUtilityLiburIntense)
                    }
                }

                setOnClickListener {
                    onItemClick(activity)
                }
            }
        }

        private fun resolveColorAttribute(colorRes: Int): Int {
            val context = binding.root.context
            val typedValue = TypedValue()
            return if (context.theme.resolveAttribute(colorRes, typedValue, true)) {
                if (typedValue.resourceId != 0) {
                    ContextCompat.getColor(context, typedValue.resourceId)
                } else {
                    typedValue.data
                }
            } else {
                try {
                    ContextCompat.getColor(context, colorRes)
                } catch (e: Exception) {
                    colorRes
                }
            }
        }
    }
}