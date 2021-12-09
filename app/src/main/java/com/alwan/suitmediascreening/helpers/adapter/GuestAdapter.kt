package com.alwan.suitmediascreening.helpers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alwan.suitmediascreening.R
import com.alwan.suitmediascreening.databinding.ItemGuestBinding
import com.alwan.suitmediascreening.repository.model.Guest

class GuestAdapter(private val listener: OnGuestClickListener) : RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {
    private val mData = ArrayList<Guest>()

    fun setData(items: ArrayList<Guest>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    interface OnGuestClickListener {
        fun onItemClicked(data: Guest)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): GuestViewHolder {
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_guest, viewGroup, false)

        return GuestViewHolder(mView)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class GuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemGuestBinding.bind(itemView)
        fun bind(guest: Guest) {
            binding.tvNamaGuest.text = guest.name
            binding.imgGuest.setImageResource(R.drawable.user)
            itemView.setOnClickListener { listener.onItemClicked(guest) }
        }
    }
}