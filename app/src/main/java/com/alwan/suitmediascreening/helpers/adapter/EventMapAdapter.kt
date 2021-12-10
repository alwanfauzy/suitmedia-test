package com.alwan.suitmediascreening.helpers.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alwan.suitmediascreening.R
import com.alwan.suitmediascreening.databinding.ItemEventBinding
import com.alwan.suitmediascreening.databinding.ItemEventMapBinding
import com.alwan.suitmediascreening.repository.model.Event

class EventMapAdapter() :
    RecyclerView.Adapter<EventMapAdapter.EventViewHolder>() {
    private val mData = ArrayList<Event>()

    fun setData(items: ArrayList<Event>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun getData(position: Int): Event {
        return mData[position]
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): EventViewHolder {
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_event_map, viewGroup, false)

        return EventViewHolder(mView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemEventMapBinding.bind(itemView)
        fun bind(event: Event) {
            binding.tvNamaEvent.text = event.nama
            binding.imgEvent.setImageResource(event.image)
        }
    }

    class MarginItemDecoration(spaceHeight: Int, private val isHorizontal: Boolean) :
        RecyclerView.ItemDecoration() {
        private val spaceHeightDp =
            (spaceHeight * Resources.getSystem().displayMetrics.density).toInt()

        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0 || isHorizontal) {
                    top = spaceHeightDp
                }
                left = spaceHeightDp
                right = spaceHeightDp
                bottom = spaceHeightDp
            }
        }
    }
}