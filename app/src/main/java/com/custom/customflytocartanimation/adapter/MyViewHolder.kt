package com.custom.customflytocartanimation.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.custom.customflytocartanimation.R

/**
 * Created by Aashis on 30,January,2023
 */
class MyViewHolder(
    private val itemView: View,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val itemIV: ImageView = itemView.findViewById(R.id.itemIV)
    val imvImage: ImageView = itemView.findViewById(R.id.itemCopyIV)
    val txvMessage: TextView = itemView.findViewById(R.id.txvMessage)

    init {
        itemIV.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }
}