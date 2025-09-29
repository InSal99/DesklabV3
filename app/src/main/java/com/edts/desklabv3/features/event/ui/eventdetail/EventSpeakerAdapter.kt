package com.edts.desklabv3.features.event.ui.eventdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.databinding.DetailInformationSpeakerBinding

class EventSpeakerAdapter : RecyclerView.Adapter<EventSpeakerAdapter.SpeakerViewHolder>() {

    private val speakers = mutableListOf<Pair<Int, String>>()

    inner class SpeakerViewHolder(private val binding: DetailInformationSpeakerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(speaker: Pair<Int, String>) {
            binding.ivDetailImage.setImageResource(speaker.first)
            binding.tvDetailName.text = speaker.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder {
        val binding = DetailInformationSpeakerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpeakerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) {
        holder.bind(speakers[position])
    }

    override fun getItemCount(): Int = speakers.size

    fun submitList(newSpeakers: List<Pair<Int, String>>) {
        speakers.clear()
        speakers.addAll(newSpeakers)
        notifyDataSetChanged()
    }
}