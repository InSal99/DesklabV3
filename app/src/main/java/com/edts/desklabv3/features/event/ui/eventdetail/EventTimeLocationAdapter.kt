package com.edts.desklabv3.features.event.ui.eventdetail

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.detail.information.DetailInformationA

class EventTimeLocationAdapter : RecyclerView.Adapter<EventTimeLocationAdapter.TimeLocationViewHolder>() {

    private val items = mutableListOf<Triple<Int, String, String>>()
    private var showActionForLinkMeeting: Boolean = false
    private var meetingLink: String = ""
    private var onActionClickListener: ((actionType: String) -> Unit)? = null

    inner class TimeLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val detailInfoView = itemView as DetailInformationA

        fun bind(item: Triple<Int, String, String>, position: Int) {
            detailInfoView.apply {
                icon = ContextCompat.getDrawable(context, item.first)
                title = item.second
                description = item.third
                hasAction = showActionForLinkMeeting && position == 3

                if (hasAction) {
                    actionButton1.text = "Bergabung Online"
                    actionButton2.text = "Copy Link"

                    actionButton1.setOnClickListener {
                        onActionClickListener?.invoke("open")
                    }

                    actionButton2.setOnClickListener {
                        onActionClickListener?.invoke("copy")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLocationViewHolder {
        val detailInfoView = DetailInformationA(parent.context)
        detailInfoView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return TimeLocationViewHolder(detailInfoView)
    }

    override fun onBindViewHolder(holder: TimeLocationViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Triple<Int, String, String>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setLinkMeetingAction(show: Boolean, link: String, listener: (String) -> Unit) {
        showActionForLinkMeeting = show
        meetingLink = link
        onActionClickListener = listener
        notifyItemChanged(3)
    }
}