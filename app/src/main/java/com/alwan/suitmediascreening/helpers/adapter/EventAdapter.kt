package com.alwan.suitmediascreening.helpers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alwan.suitmediascreening.R
import com.alwan.suitmediascreening.databinding.ItemEventBinding
import com.alwan.suitmediascreening.repository.model.Event

class EventAdapter(private val listener: OnEventClickListener) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private val mData = ArrayList<Event>()

    fun setData(items: ArrayList<Event>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    interface OnEventClickListener {
        fun onItemClicked(data: Event)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): EventViewHolder {
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_event, viewGroup, false)

        return EventViewHolder(mView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemEventBinding.bind(itemView)
        fun bind(event: Event) {
            binding.tvNamaEvent.text = event.nama
            binding.tvTanggalEvent.text = event.tanggal
            binding.imgEvent.setImageResource(event.image)
            itemView.setOnClickListener { listener.onItemClicked(event) }
        }
    }
}